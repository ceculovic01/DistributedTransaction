package com.raf.transactionalcore.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ServiceParticipantDto {

    private String name;
    private String url;
    private String httpMethod;
    private Object body;
}
