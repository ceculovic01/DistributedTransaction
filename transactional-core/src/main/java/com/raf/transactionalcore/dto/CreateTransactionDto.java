package com.raf.transactionalcore.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateTransactionDto {

    List<ServiceParticipantDto> serviceParticipantDtoList;
}
