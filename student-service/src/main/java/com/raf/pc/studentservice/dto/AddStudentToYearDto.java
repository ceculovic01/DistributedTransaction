package com.raf.pc.studentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class AddStudentToYearDto {

    private UUID studentUUID;
    private Integer year;
    private List<String> subjects;
}
