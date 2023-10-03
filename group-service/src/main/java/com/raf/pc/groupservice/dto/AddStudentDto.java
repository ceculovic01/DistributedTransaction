package com.raf.pc.groupservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class AddStudentDto {

    @NotNull(message = "Student uuid cannot be empty")
    private UUID studentUUID;
    @NotBlank(message = "Given name for group cannot be empty")
    @Size(min = 3, max = 255, message = "Group name must be at least 3 characters long")
    private String name;
}
