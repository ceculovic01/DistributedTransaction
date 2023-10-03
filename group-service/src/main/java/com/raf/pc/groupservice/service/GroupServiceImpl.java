package com.raf.pc.groupservice.service;

import com.raf.pc.groupservice.dto.AddStudentDto;
import com.raf.pc.groupservice.dto.AddUpdateGroupDto;
import com.raf.pc.groupservice.dto.GroupDto;
import com.raf.pc.groupservice.dto.StudentDto;
import com.raf.pc.groupservice.exception.BadRequestException;
import com.raf.pc.groupservice.exception.NotFoundException;
import com.raf.pc.groupservice.mapper.GroupMapper;
import com.raf.pc.groupservice.model.Group;
import com.raf.pc.groupservice.model.Student;
import com.raf.pc.groupservice.repository.GroupRepository;
import com.raf.pc.groupservice.repository.StudentRepository;
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
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final GroupMapper groupMapper;
    private final ApplicationEventPublisher publisher;

    public GroupServiceImpl(GroupRepository groupRepository,
                            StudentRepository studentRepository,
                            GroupMapper groupMapper,
                            ApplicationEventPublisher publisher) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.groupMapper = groupMapper;
        this.publisher = publisher;
    }

    @Async
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void saveStudent(UUID txUUID, Student student) {
        log.info("Adding student for transaction with id '{}'..", txUUID);
        publisher.publishEvent(new TransactionEvent(txUUID));
        studentRepository.save(student);
    }

    @Override
    public List<GroupDto> findAllGroups() {
        return groupRepository.findAll()
                .stream()
                .map(groupMapper::modelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public GroupDto createGroup(AddUpdateGroupDto groupDto) {
        log.info("Creating new group with name '{}'", groupDto.getName());

        groupRepository.findByName(groupDto.getName()).ifPresent(g -> {
            log.error("Group with name '{}' already exists", g.getName());
            throw new BadRequestException("Group with given name already exists");
        });

        Group group = groupRepository.save(groupMapper.addUpdateDtoToModel(groupDto));

        return groupMapper.modelToDto(group);
    }

    @Override
    public GroupDto findGroupById(Long id) {
        log.info("Getting group with id '{}'", id);
        Group group = groupRepository.findById(id).orElseThrow(() -> {
            log.error("Group with id '{}' does not exist", id);
            throw new NotFoundException("Group with given id does not exist");
        });

        return groupMapper.modelToDto(group);
    }

    @Override
    public GroupDto removeGroup(Long id) {
        log.info("Trying to delete group with id '{}'", id);
        Group group = groupRepository.findById(id).orElseThrow(() -> {
            log.error("Group with id '{}' does not exist", id);
            throw new NotFoundException("Group with given id does not exist");
        });
        groupRepository.delete(group);
        return groupMapper.modelToDto(group);
    }

    @Override
    public GroupDto updateGroup(Long id, AddUpdateGroupDto groupDto) {
        Group group = groupRepository.findById(id).orElseThrow(() -> {
            log.error("Group with id '{}' does not exist", id);
            throw new NotFoundException("Group with given id does not exist");
        });

        group.setName(groupDto.getName());

        return groupMapper.modelToDto(groupRepository.save(group));
    }

    @Override
    public Student validateStudentForGroup(AddStudentDto addStudentDto) {
        Group group = groupRepository.findByName(addStudentDto.getName()).orElseThrow(() -> {
            log.error("Group with name '{}' does not exist", addStudentDto.getName());
            throw new NotFoundException("Group with given name does not exist");
        });
        group.getStudents()
                .stream()
                .filter((s) -> s.getStudentUUID().equals(addStudentDto.getStudentUUID()))
                .findFirst()
                .ifPresent((s) -> {
                    log.error("Student with given uuid is already added in group");
                    throw new BadRequestException("Student with given uuid is already added in group");
                });

        return groupMapper.addStudentDtoToModel(addStudentDto.getStudentUUID(), group);
    }


    @Override
    public StudentDto removeStudentFromGroup(UUID studentUUID) {
        Student student = studentRepository.findByStudentUUID(studentUUID).orElseThrow(() -> {
            log.error("Student with uuid '{}' does not exist", studentUUID);
            throw new NotFoundException("Student with given uuid does not exist");
        });

        log.info("Removing student with uuid '{}'", studentUUID);
        studentRepository.delete(student);

        return groupMapper.modelToStudentDto(student);
    }
}
