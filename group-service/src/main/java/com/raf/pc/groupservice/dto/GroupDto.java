package com.raf.pc.groupservice.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class GroupDto {

    private Long id;

    private String name;

    private Set<StudentDto> students = new HashSet<>();
}
