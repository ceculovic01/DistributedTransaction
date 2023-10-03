package com.raf.pc.studentmanagement.controller;

import com.raf.pc.studentmanagement.dto.AddStudentDto;
import com.raf.pc.studentmanagement.dto.AddStudentYearDto;
import com.raf.pc.studentmanagement.dto.StudentYearDto;
import com.raf.pc.studentmanagement.dto.SubjectDto;
import com.raf.pc.studentmanagement.model.Student;
import com.raf.pc.studentmanagement.service.StudentYearService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/student-year")
public class StudentYearController {

    private final StudentYearService studentYearService;


    public StudentYearController(StudentYearService studentYearService) {
        this.studentYearService = studentYearService;
    }

    @GetMapping
    public ResponseEntity<List<StudentYearDto>> findAll() {
        return ResponseEntity.ok(studentYearService.findAll());
    }

    @PostMapping
    public ResponseEntity<StudentYearDto> createStudentYear(@RequestBody @Valid AddStudentYearDto addStudentYearDto) {
        return ResponseEntity.ok(studentYearService.addStudentYear(addStudentYearDto));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StudentYearDto> findStudentYearById(@PathVariable Long id) {
        return ResponseEntity.ok(studentYearService.findStudentYearById(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<StudentYearDto> deleteStudentYearById(@PathVariable Long id) {
        return ResponseEntity.ok(studentYearService.deleteStudentYearById(id));
    }

    @PostMapping(value = "/student")
    public ResponseEntity<?> addStudentToYear(@RequestHeader("txn-header") String header,
                                              @RequestBody @Valid AddStudentDto addStudentDto) {
        UUID txnUUID = UUID.fromString(header);
        Student student = studentYearService.validateStudent(addStudentDto);
        studentYearService.addStudentToStudentYear(txnUUID, student);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/subject")
    public ResponseEntity<SubjectDto> createSubject(@RequestBody @Valid SubjectDto subjectDto) {
        return ResponseEntity.ok(studentYearService.createSubject(subjectDto));
    }

    @GetMapping(value = "/subject")
    public ResponseEntity<List<SubjectDto>> findAllSubjects() {
        return ResponseEntity.ok(studentYearService.getAllSubjects());
    }
}
