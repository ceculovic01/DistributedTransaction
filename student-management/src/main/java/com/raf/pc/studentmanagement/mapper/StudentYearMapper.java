package com.raf.pc.studentmanagement.mapper;

import com.raf.pc.studentmanagement.dto.AddStudentYearDto;
import com.raf.pc.studentmanagement.dto.StudentDto;
import com.raf.pc.studentmanagement.dto.StudentYearDto;
import com.raf.pc.studentmanagement.dto.SubjectDto;
import com.raf.pc.studentmanagement.model.Student;
import com.raf.pc.studentmanagement.model.StudentYear;
import com.raf.pc.studentmanagement.model.Subject;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class StudentYearMapper {

    public StudentYear addStudentYearDtoToModel(AddStudentYearDto addStudentYearDto) {
        StudentYear studentYear = new StudentYear();
        studentYear.setYear(addStudentYearDto.getYear());

        return studentYear;
    }

    public StudentYearDto modelToDto(StudentYear studentYear) {
        StudentYearDto studentYearDto = new StudentYearDto();
        studentYearDto.setYear(studentYear.getYear());
        studentYearDto.setId(studentYear.getId());
        studentYearDto.setStudents(
                studentYear.getStudents()
                        .stream()
                        .map(this::modelToStudentDto)
                        .collect(Collectors.toSet())
        );

        return studentYearDto;
    }

    public StudentDto modelToStudentDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentUUID(student.getStudentUUID());

        return studentDto;
    }

    public Student addStudentDtoToModel(UUID studentUUID, StudentYear studentYear, Set<Subject> subjects) {
        Student student = new Student();
        student.setStudentUUID(studentUUID);
        student.setStudentYear(studentYear);
        student.setSubjects(subjects);

        return student;
    }

    public Subject subjectDtoToModel(SubjectDto subjectDto) {
        Subject subject = new Subject();
        subject.setName(subjectDto.getName());
        return subject;
    }

    public SubjectDto subjectModelToDto(Subject subject) {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(subject.getId());
        subjectDto.setName(subject.getName());

        return subjectDto;
    }
}
