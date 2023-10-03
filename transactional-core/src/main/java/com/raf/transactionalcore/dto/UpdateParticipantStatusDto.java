package com.raf.transactionalcore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateParticipantStatusDto {

    private String participantName;
    private TransactionStatus transactionStatus;
}
