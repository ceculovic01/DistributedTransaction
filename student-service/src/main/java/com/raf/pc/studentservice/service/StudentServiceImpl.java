package com.raf.pc.studentservice.service;

import com.raf.pc.studentservice.dto.CreateStudentDto;
import com.raf.pc.studentservice.dto.StudentDto;
import com.raf.pc.studentservice.exception.BadRequestException;
import com.raf.pc.studentservice.exception.NotFoundException;
import com.raf.pc.studentservice.mapper.StudentMapper;
import com.raf.pc.studentservice.model.Student;
import com.raf.pc.studentservice.repository.StudentRepository;
import com.raf.transactionalcore.listener.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;
    private final StudentMapper mapper;
    private final ApplicationEventPublisher publisher;

    public StudentServiceImpl(StudentRepository repository, StudentMapper mapper,
                              ApplicationEventPublisher publisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    @Override
    public Student validateStudent(CreateStudentDto createStudentDto) {
        repository.findByIndex(createStudentDto.getIndex()).ifPresent((student -> {
            log.error("Student with index '{}' already exists", createStudentDto.getIndex());
            throw new BadRequestException("Student with given index already exists");
        }));

        return mapper.dtoToModel(createStudentDto);
    }

    @Async
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void saveStudent(UUID txnUUID, Student student) {
        log.info("Saving student for transaction with uuid '{}'", txnUUID);
        publisher.publishEvent(new TransactionEvent(txnUUID));
        repository.save(student);
    }

    @Override
    public List<StudentDto> findAll() {
        log.info("Getting all students...");
        return repository.findAll()
                .stream()
                .map(mapper::modelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto findStudentByUUID(UUID uuid) {
        log.info("Getting student with uuid '{}'", uuid);
        Student student = repository.findByStudentUUID(uuid).orElseThrow(() -> {
            log.error("Student with uuid '{}' does not exist", uuid);
            throw new NotFoundException("Student with given uuid does not exist");
        });
        return mapper.modelToDto(student);
    }
}
