package com.raf.pc.studentservice.controller;

import com.raf.pc.studentservice.dto.CreateStudentDto;
import com.raf.pc.studentservice.dto.MessageDto;
import com.raf.pc.studentservice.dto.StudentDto;
import com.raf.pc.studentservice.model.Student;
import com.raf.pc.studentservice.service.CoordinatorCaller;
import com.raf.pc.studentservice.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/student")
public class StudentController {

    private final StudentService studentService;
    private final CoordinatorCaller coordinatorCaller;

    public StudentController(StudentService studentService,
                             CoordinatorCaller coordinatorCaller) {
        this.coordinatorCaller = coordinatorCaller;
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<MessageDto> createStudent(@RequestBody @Valid CreateStudentDto createStudentDto) {
        Student student = studentService.validateStudent(createStudentDto);
        coordinatorCaller.createTransaction(createStudentDto, student);
        return ResponseEntity.ok(new MessageDto("Transaction started"));
    }

    @GetMapping(value = "/{uuid}")
    public ResponseEntity<StudentDto> findStudentByUUID(@PathVariable UUID uuid) {
        return ResponseEntity.ok(studentService.findStudentByUUID(uuid));
    }

    @PostMapping(value = "/transaction")
    public ResponseEntity<?> saveStudent(@RequestHeader(value = "txn-header") String txnUUID,
                                         @RequestBody Student student) {
        UUID uuid = UUID.fromString(txnUUID);
        studentService.saveStudent(uuid, student);
        return ResponseEntity.ok().build();
    }
}
