package com.raf.pc.studentservice.service;

import com.raf.pc.studentservice.dto.CreateStudentDto;
import com.raf.pc.studentservice.dto.StudentDto;
import com.raf.pc.studentservice.model.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    Student validateStudent(CreateStudentDto createStudentDto);

    void saveStudent(UUID txnUUID, Student student);

    List<StudentDto> findAll();

    StudentDto findStudentByUUID(UUID uuid);
}
