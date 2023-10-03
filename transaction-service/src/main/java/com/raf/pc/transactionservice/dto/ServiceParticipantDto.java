package com.raf.pc.transactionservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ServiceParticipantDto {

    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotBlank(message = "URL cannot be empty")
    private String url;
    @NotBlank(message = "Http method cannot be null")
    private String httpMethod;
    @NotNull(message = "Body cannot be null")
    private Object body;
}
