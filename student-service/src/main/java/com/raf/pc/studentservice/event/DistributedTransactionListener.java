package com.raf.pc.studentservice.event;

import com.raf.transactionalcore.dto.TransactionMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DistributedTransactionListener {

    private final EventBus eventBus;
    private final String applicationName;

    public DistributedTransactionListener(EventBus eventBus,
                                          @Value("${spring.application.name}") String applicationName) {
        this.eventBus = eventBus;
        this.applicationName = applicationName;
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue("txn-exchange-student"), exchange = @Exchange(type = ExchangeTypes.TOPIC, name = "txn-exchange"))
    })
    public void receiveMessage(TransactionMessageDto messageDto) {
        if (messageDto.getServiceNames().contains(applicationName)) {
            log.info("Received message for transaction with id '{}' with status '{}'",
                    messageDto.getTransactionUUID(), messageDto.getTransactionMessage());
            eventBus.sendTransaction(messageDto);
        }
    }
}
