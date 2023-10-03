package com.raf.pc.studentservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AddStudentToGroupDto {

    private final UUID studentUUID;
    private final String name;
}
