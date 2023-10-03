package com.raf.pc.transactionservice.service;

import com.raf.pc.transactionservice.dto.CreateTransactionDto;
import com.raf.pc.transactionservice.dto.UpdateParticipantStatusDto;

import java.util.UUID;

public interface DistributedTransactionService {

    void updateParticipantStatus(UUID transactionUUID,
                                        UpdateParticipantStatusDto updateParticipantStatusDto);

    void createDistributedTransaction(CreateTransactionDto createTransactionDto);
}
