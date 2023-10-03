package com.raf.pc.studentservice.mapper;

import com.raf.pc.studentservice.dto.CreateStudentDto;
import com.raf.pc.studentservice.dto.StudentDto;
import com.raf.pc.studentservice.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDto modelToDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentUUID(student.getStudentUUID());
        studentDto.setIndex(student.getIndex());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());

        return studentDto;
    }

    public Student dtoToModel(CreateStudentDto createStudentDto) {
        Student student = new Student();
        student.setIndex(createStudentDto.getIndex());
        student.setFirstName(createStudentDto.getFirstName());
        student.setLastName(createStudentDto.getLastName());

        return student;
    }
}
