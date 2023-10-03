package com.raf.transactionalcore.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class TransactionEvent {

    private final UUID txnId;
}
