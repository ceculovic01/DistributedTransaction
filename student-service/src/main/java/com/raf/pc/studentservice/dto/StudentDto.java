package com.raf.pc.studentservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class StudentDto {

    private UUID studentUUID;
    private String firstName;
    private String lastName;
    private String index;
}
