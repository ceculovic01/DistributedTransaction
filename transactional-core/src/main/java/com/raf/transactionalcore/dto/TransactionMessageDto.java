package com.raf.transactionalcore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionMessageDto implements Serializable {

    private UUID transactionUUID;

    private TransactionMessage transactionMessage;

    private List<String> serviceNames;
}
