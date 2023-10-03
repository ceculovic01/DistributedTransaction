package com.raf.pc.studentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubjectDto {

    private Long id;
    @NotBlank(message = "Subject name cannot be empty")
    @Size(min = 3, max = 255, message = "Subject name must be atleast 3 characters long")
    private String name;
}
