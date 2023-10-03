package com.raf.pc.studentmanagement.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class StudentYearDto {

    private Long id;

    private Integer year;

    private Set<StudentDto> students = new HashSet<>();
}
