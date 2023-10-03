package com.raf.pc.studentmanagement.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddStudentYearDto {

    @NotNull(message = "Year cannot be empty")
    @Min(value = 1, message = "Year value is invalid")
    @Max(value = 4, message = "Year value is invalid")
    private final Integer year;
}
