package com.raf.pc.groupservice.event;

import com.raf.transactionalcore.dto.TransactionMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class EventBus {

    private final List<TransactionMessageDto> messages;

    public EventBus() {
        this.messages = new ArrayList<>();
    }

    public void sendTransaction(TransactionMessageDto message) {
        messages.add(message);
    }

    public TransactionMessageDto receiveTransaction(UUID transactionUUID) {
        TransactionMessageDto message = messages.stream().filter(m -> m.getTransactionUUID().equals(transactionUUID)).findAny().orElse(null);
        if (message != null) {
            messages.remove(message);
        }
        return message;
    }

}
