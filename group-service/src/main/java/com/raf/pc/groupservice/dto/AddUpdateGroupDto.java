package com.raf.pc.groupservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddUpdateGroupDto {

    @NotBlank(message = "Given name for group cannot be empty")
    @Size(min = 3, max = 255, message = "Group name must be at least 3 characters long")
    private String name;
}
