package com.raf.pc.studentmanagement.service;

import com.raf.pc.studentmanagement.dto.AddStudentDto;
import com.raf.pc.studentmanagement.dto.AddStudentYearDto;
import com.raf.pc.studentmanagement.dto.StudentYearDto;
import com.raf.pc.studentmanagement.dto.SubjectDto;
import com.raf.pc.studentmanagement.exception.BadRequestException;
import com.raf.pc.studentmanagement.exception.NotFoundException;
import com.raf.pc.studentmanagement.mapper.StudentYearMapper;
import com.raf.pc.studentmanagement.model.Student;
import com.raf.pc.studentmanagement.model.StudentYear;
import com.raf.pc.studentmanagement.model.Subject;
import com.raf.pc.studentmanagement.repository.StudentRepository;
import com.raf.pc.studentmanagement.repository.StudentYearRepository;
import com.raf.pc.studentmanagement.repository.SubjectRepository;
import com.raf.transactionalcore.listener.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StudentYearServiceImpl implements StudentYearService {

    private final StudentYearRepository studentYearRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final StudentYearMapper mapper;
    private final ApplicationEventPublisher publisher;

    public StudentYearServiceImpl(StudentYearRepository studentYearRepository,
                                  StudentRepository studentRepository,
                                  SubjectRepository subjectRepository,
                                  StudentYearMapper mapper,
                                  ApplicationEventPublisher publisher) {
        this.studentYearRepository = studentYearRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    @Override
    public List<StudentYearDto> findAll() {
        return studentYearRepository.findAll()
                .stream()
                .map(mapper::modelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentYearDto findStudentYearById(Long id) {
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow(() -> {
            log.info("Student year with id '{}' does not exist", id);
            throw new NotFoundException("Student year with given id does not exist");
        });
        return mapper.modelToDto(studentYear);
    }

    @Override
    public StudentYearDto addStudentYear(AddStudentYearDto addStudentYearDto) {
        studentYearRepository.findByYear(addStudentYearDto.getYear()).ifPresent((s) -> {
            log.info("Student year '{}' already exist", s.getYear());
            throw new BadRequestException("Given student year already exists");
        });

        StudentYear studentYear = mapper.addStudentYearDtoToModel(addStudentYearDto);
        log.info("Adding new student year '{}'", studentYear);
        return mapper.modelToDto(studentYearRepository.save(studentYear));
    }

    @Override
    public StudentYearDto deleteStudentYearById(Long id) {
        log.info("Trying to delete student year with id '{}'", id);
        StudentYear studentYear = studentYearRepository.findById(id).orElseThrow(() -> {
            log.info("Student year with id '{}' does not exist", id);
            throw new NotFoundException("Student year with given id does not exist");
        });
        studentYearRepository.delete(studentYear);
        log.info("Successfully deleted studentYear '{}'", studentYear.getYear());
        return mapper.modelToDto(studentYear);
    }

    @Override
    public Student validateStudent(AddStudentDto addStudentDto) {
        StudentYear studentYear = studentYearRepository.findByYear(addStudentDto.getYear()).orElseThrow(() -> {
            log.info("Student year with name '{}' does not exist", addStudentDto.getYear());
            throw new NotFoundException("Student year with given name does not exist");
        });

        studentYear.getStudents()
                .stream()
                .filter(s -> s.getStudentUUID().equals(addStudentDto.getStudentUUID()))
                .findFirst()
                .ifPresent((s) -> {
                    log.info("Student with uuid '{}' already is added to year '{}'",
                            s.getStudentUUID(), studentYear.getYear());
                    throw new BadRequestException("Student with given uuid is already added to given year");
                });

        Set<Subject> subjects = subjectRepository.getSubjectsInList(addStudentDto.getSubjects());

        return mapper.addStudentDtoToModel(addStudentDto.getStudentUUID(), studentYear, subjects);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Async
    @Override
    public void addStudentToStudentYear(UUID txUUID, Student student) {
        log.info("Adding student for transaction with id '{}'..", txUUID);
        publisher.publishEvent(new TransactionEvent(txUUID));
        studentRepository.save(student);
    }

    @Override
    public SubjectDto createSubject(SubjectDto addSubjectDto) {
        log.info("Adding subject with name '{}'", addSubjectDto.getName());
        subjectRepository.findByName(addSubjectDto.getName()).ifPresent((s) -> {
            log.error("Subject '{}' already exists", addSubjectDto.getName());
            throw new BadRequestException("Subject with given name already exists");
        });

        Subject subject = subjectRepository.save(mapper.subjectDtoToModel(addSubjectDto));

        log.info("Subject with name '{}' successfully saved", subject.getName());
        return mapper.subjectModelToDto(subject);
    }

    @Override
    public List<SubjectDto> getAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(mapper::subjectModelToDto)
                .collect(Collectors.toList());
    }
}
