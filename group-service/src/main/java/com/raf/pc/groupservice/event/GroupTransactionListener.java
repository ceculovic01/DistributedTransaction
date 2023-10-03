package com.raf.pc.groupservice.event;

import com.raf.pc.groupservice.exception.ConflictException;
import com.raf.transactionalcore.dto.TransactionMessage;
import com.raf.transactionalcore.dto.TransactionMessageDto;
import com.raf.transactionalcore.dto.TransactionStatus;
import com.raf.transactionalcore.dto.UpdateParticipantStatusDto;
import com.raf.transactionalcore.listener.TransactionEvent;
import com.raf.transactionalcore.listener.TransactionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class GroupTransactionListener implements TransactionListener<TransactionEvent> {

    private final EventBus eventBus;
    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;
    private final String serviceName;

    public GroupTransactionListener(EventBus eventBus,
                                    RestTemplate restTemplate,
                                    RetryTemplate retryTemplate,
                                    @Value("${spring.application.name}") String serviceName) {
        this.eventBus = eventBus;
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
        this.serviceName = serviceName;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Override
    public void handleEvent(TransactionEvent event) {
        log.info("Waiting for confirmation for transaction with id '{}' before commit..", event.getTxnId());
        int count = 3000;
        TransactionMessageDto transactionMessageDto = null;
        while (count > 0) {
            transactionMessageDto = eventBus.receiveTransaction(event.getTxnId());
            if (transactionMessageDto != null) {
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                log.error("Error while receiving transaction for '{}' with cause '{}'", event.getTxnId(), e);
            }
            count--;
        }

        if (transactionMessageDto == null || transactionMessageDto.getTransactionMessage() == TransactionMessage.TO_ROLLBACK) {
            throw new ConflictException(String.format("Could not process transaction with id '%s'", event.getTxnId()));
        }
        log.info("Committing transaction with id '{}'", event.getTxnId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    @Override
    public void handleAfterRollback(TransactionEvent event) {
        log.info("After rollback of transaction with id '{}'", event.getTxnId());
        notifyCoordinator(TransactionStatus.ROLLBACKED, event.getTxnId());

    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void handleAfterCompletion(TransactionEvent event) {
        log.info("After commit of transaction with id '{}'", event.getTxnId());
        notifyCoordinator(TransactionStatus.COMMITED, event.getTxnId());

    }

    private void notifyCoordinator(TransactionStatus transactionStatus, UUID txnUUID) {
        UpdateParticipantStatusDto updateParticipantStatusDto = new UpdateParticipantStatusDto(serviceName, transactionStatus);
        HttpEntity<UpdateParticipantStatusDto> httpEntity = new HttpEntity<>(updateParticipantStatusDto);
        log.info("Sending transaction status '{}' for transaction with id '{}'", transactionStatus, txnUUID);
        retryTemplate.execute(context -> restTemplate.exchange("http://localhost:8084/api/transaction/" + txnUUID.toString(),
                HttpMethod.PUT, httpEntity, UpdateParticipantStatusDto.class));
    }
}
