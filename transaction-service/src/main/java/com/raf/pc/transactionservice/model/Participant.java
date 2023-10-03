package com.raf.pc.transactionservice.model;

import com.raf.transactionalcore.dto.TransactionStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Participant {

    private String name;

    private TransactionStatus transactionStatus = TransactionStatus.PREPARING;
}
