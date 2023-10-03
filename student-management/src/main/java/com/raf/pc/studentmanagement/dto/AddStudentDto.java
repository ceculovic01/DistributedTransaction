package com.raf.pc.studentmanagement.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AddStudentDto {

    @NotNull(message = "Student uuid cannot be empty")
    private UUID studentUUID;
    @NotNull(message = "Year cannot be empty")
    @Min(value = 1, message = "Year value is invalid")
    @Max(value = 4, message = "Year value is invalid")
    private Integer year;
    @NotNull(message = "Subject list cannot be empty")
    private List<String> subjects;
}
