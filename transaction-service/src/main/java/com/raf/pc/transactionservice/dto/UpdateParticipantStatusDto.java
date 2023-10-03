package com.raf.pc.transactionservice.dto;

import com.raf.transactionalcore.dto.TransactionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateParticipantStatusDto {

    @NotBlank(message = "Participant name cannot be empty")
    private String participantName;
    @NotNull(message = "Transactions status cannot be null")
    private TransactionStatus transactionStatus;
}
