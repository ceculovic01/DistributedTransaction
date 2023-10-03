package com.raf.pc.studentservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateStudentDto {

    @Size(min = 2, max = 255, message = "First name must be at least 2 characters long")
    @NotBlank(message = "First name cannot be empty")
    private String firstName;
    @Size(min = 2, max = 255, message = "Last name must be at least 2 characters long")
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;
    @Size(min = 8, max = 255, message = "First name must be at least 2 characters long")
    @NotBlank(message = "Index cannot be empty")
    private String index;
    @Size(min = 3, max = 255, message = "Group name must be at least 3 characters long")
    @NotBlank(message = "Group name cannot be empty")
    private String groupName;
    @NotNull(message = "Year cannot be null")
    @Min(value = 1, message = "Year value invalid")
    @Max(value = 4, message = "Year value invalid")
    private Integer year;
    @NotNull(message = "Subject list cannot be null")
    private List<String> subjects;
}
