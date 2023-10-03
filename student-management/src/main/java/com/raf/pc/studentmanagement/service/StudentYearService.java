package com.raf.pc.studentmanagement.service;

import com.raf.pc.studentmanagement.dto.AddStudentDto;
import com.raf.pc.studentmanagement.dto.AddStudentYearDto;
import com.raf.pc.studentmanagement.dto.StudentYearDto;
import com.raf.pc.studentmanagement.dto.SubjectDto;
import com.raf.pc.studentmanagement.model.Student;

import java.util.List;
import java.util.UUID;

public interface StudentYearService {

    List<StudentYearDto> findAll();

    StudentYearDto findStudentYearById(Long id);

    StudentYearDto addStudentYear(AddStudentYearDto addStudentYearDto);

    StudentYearDto deleteStudentYearById(Long id);

    Student validateStudent(AddStudentDto addStudentDto);

    void addStudentToStudentYear(UUID txUUID, Student student);

    SubjectDto createSubject(SubjectDto addSubjectDto);

    List<SubjectDto> getAllSubjects();
}
