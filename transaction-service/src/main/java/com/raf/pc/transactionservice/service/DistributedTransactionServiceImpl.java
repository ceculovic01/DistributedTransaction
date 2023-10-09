package com.raf.pc.transactionservice.service;

import com.raf.pc.transactionservice.dto.CreateTransactionDto;
import com.raf.pc.transactionservice.dto.ServiceParticipantDto;
import com.raf.pc.transactionservice.dto.UpdateParticipantStatusDto;
import com.raf.pc.transactionservice.exception.NotFoundException;
import com.raf.pc.transactionservice.model.DistributedTransaction;
import com.raf.pc.transactionservice.model.Participant;
import com.raf.pc.transactionservice.model.RollbackInformation;
import com.raf.pc.transactionservice.repository.DistributedTransactionRepository;
import com.raf.transactionalcore.dto.TransactionMessage;
import com.raf.transactionalcore.dto.TransactionMessageDto;
import com.raf.transactionalcore.dto.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DistributedTransactionServiceImpl implements DistributedTransactionService {

    private final DistributedTransactionRepository repository;
    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;
    private final RabbitTemplate rabbitTemplate;

    public DistributedTransactionServiceImpl(DistributedTransactionRepository repository,
                                             RestTemplate restTemplate,
                                             RetryTemplate retryTemplate,
                                             RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void updateParticipantStatus(UUID transactionUUID,
                                        UpdateParticipantStatusDto updateParticipantStatusDto) {
        log.info("Updating transaction status to '{}' for participant '{}'", updateParticipantStatusDto.getTransactionStatus(), updateParticipantStatusDto.getParticipantName());

        DistributedTransaction distributedTransaction = findByIdAndUpdateParticipantStatus(transactionUUID, updateParticipantStatusDto);

        List<TransactionStatus> transactionStatuses = distributedTransaction.getParticipants()
                .stream()
                .map(Participant::getTransactionStatus)
                .collect(Collectors.toList());

        if (transactionStatuses.contains(TransactionStatus.PREPARING)) {
            return;
        }

        if (transactionStatuses.contains(TransactionStatus.ROLLBACKED)) {
            distributedTransaction.setTransactionStatus(TransactionStatus.ROLLBACKED);
        } else {
            distributedTransaction.setTransactionStatus(TransactionStatus.COMMITED);
        }
        distributedTransaction.setEnded(LocalDateTime.now());
        repository.save(distributedTransaction);
    }

    private DistributedTransaction findByIdAndUpdateParticipantStatus(UUID transactionUUID, UpdateParticipantStatusDto updateParticipantStatusDto) {
        try {
            DistributedTransaction distributedTransaction = repository.findByTransactionUUID(transactionUUID)
                    .orElseThrow(() -> {
                        log.error("Transaction with uuid '{}' does not exist", transactionUUID);
                        throw new NotFoundException("Transaction with given uuid does not exist");
                    });

            distributedTransaction.getParticipants()
                    .stream()
                    .filter(p -> p.getName().equals(updateParticipantStatusDto.getParticipantName()))
                    .findFirst()
                    .ifPresent(p -> p.setTransactionStatus(updateParticipantStatusDto.getTransactionStatus()));

            return repository.save(distributedTransaction);
        } catch (OptimisticLockingFailureException e) {
            return findByIdAndUpdateParticipantStatus(transactionUUID, updateParticipantStatusDto);
        }
    }

    @Async
    @Override
    public void createDistributedTransaction(CreateTransactionDto createTransactionDto) {
        DistributedTransaction distributedTransaction = new DistributedTransaction();
        distributedTransaction.setMetadata(createTransactionDto.getMetadata());
        List<Participant> participants = new ArrayList<>();
        for (ServiceParticipantDto spd : createTransactionDto.getServiceParticipantDtoList()) {
            Participant participant = new Participant();
            participant.setName(spd.getName());
            participants.add(participant);
        }
        distributedTransaction.setParticipants(participants);

        boolean isPrepared = true;
        List<String> serviceNames = new ArrayList<>();

        for (Participant p : participants) {
            try {
                if (!isPrepared) {
                    p.setTransactionStatus(TransactionStatus.ROLLBACKED);
                    continue;
                }
                ServiceParticipantDto spd = createTransactionDto.getServiceParticipantDtoList()
                        .stream()
                        .filter(s -> s.getName().equals(p.getName()))
                        .findFirst()
                        .get();

                callParticipant(distributedTransaction.getTransactionUUID(), spd);
                serviceNames.add(p.getName());
            } catch (RestClientException e) {
                log.error("Got exception from service '{}'", p.getName(), e);
                isPrepared = false;
                p.setTransactionStatus(TransactionStatus.ROLLBACKED);
                addRollbackInformation(e, distributedTransaction, p.getName());
            }
        }

        if (serviceNames.isEmpty()) {
            distributedTransaction.setTransactionStatus(TransactionStatus.ROLLBACKED);
            distributedTransaction.setEnded(LocalDateTime.now());
            repository.save(distributedTransaction);
            return;
        }

        distributedTransaction = repository.save(distributedTransaction);

        TransactionMessage status = isPrepared ? TransactionMessage.TO_COMMIT : TransactionMessage.TO_ROLLBACK;
        rabbitTemplate.convertAndSend(new TransactionMessageDto(distributedTransaction.getTransactionUUID(), status, serviceNames));
    }

    private void callParticipant(UUID txnUUID, ServiceParticipantDto serviceParticipantDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("txn-header", txnUUID.toString());
        HttpEntity<Object> httpEntity = new HttpEntity<>(serviceParticipantDto.getBody(), headers);
        retryTemplate.execute(context ->
                restTemplate.exchange(serviceParticipantDto.getUrl(), HttpMethod.valueOf(serviceParticipantDto.getHttpMethod()), httpEntity, Object.class)
        );
    }

    private void addRollbackInformation(RestClientException restClientException, DistributedTransaction distributedTransaction,
                                        String serviceName) {
        RollbackInformation rollbackInformation = new RollbackInformation();
        rollbackInformation.setServiceName(serviceName);
        if (restClientException instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientErrorException = (HttpClientErrorException) restClientException;
            rollbackInformation.setStatusCode(httpClientErrorException.getStatusCode().toString());
            rollbackInformation.setResponseBody(httpClientErrorException.getResponseBodyAsString());
        } else {
            rollbackInformation.setResponseBody(restClientException.getMessage());
        }
        distributedTransaction.setRollbackInformation(rollbackInformation);
    }
}
