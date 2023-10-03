package com.raf.pc.transactionservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CreateTransactionDto {

    private List<ServiceParticipantDto> serviceParticipantDtoList;
    private Map<String, Object> metadata;
}
