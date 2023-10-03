package com.raf.pc.transactionservice.controller;

import com.raf.pc.transactionservice.dto.CreateTransactionDto;
import com.raf.pc.transactionservice.dto.UpdateParticipantStatusDto;
import com.raf.pc.transactionservice.service.DistributedTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    private final DistributedTransactionService service;

    public TransactionController(DistributedTransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody @Valid CreateTransactionDto createTransactionDto) {
        service.createDistributedTransaction(createTransactionDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateParticipantStatus(@PathVariable UUID id,
                                                     @RequestBody @Valid UpdateParticipantStatusDto updateParticipantStatusDto) {
        service.updateParticipantStatus(id, updateParticipantStatusDto);
        return ResponseEntity.ok().build();
    }
}
