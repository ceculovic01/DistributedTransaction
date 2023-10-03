package com.raf.pc.studentservice.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreateTransactionDto {

    private List<ServiceParticipantDto> serviceParticipantDtoList;

    private Map<String, Object> metadata;

}
