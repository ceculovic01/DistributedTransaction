package com.raf.pc.groupservice.service;

import com.raf.pc.groupservice.dto.AddStudentDto;
import com.raf.pc.groupservice.dto.AddUpdateGroupDto;
import com.raf.pc.groupservice.dto.GroupDto;
import com.raf.pc.groupservice.dto.StudentDto;
import com.raf.pc.groupservice.model.Student;

import java.util.List;
import java.util.UUID;

public interface GroupService {

    List<GroupDto> findAllGroups();

    GroupDto createGroup(AddUpdateGroupDto groupDto);

    GroupDto findGroupById(Long id);

    GroupDto removeGroup(Long id);

    GroupDto updateGroup(Long id, AddUpdateGroupDto groupDto);

    Student validateStudentForGroup(AddStudentDto addStudentDto);

    StudentDto removeStudentFromGroup(UUID studentUUID);

    void saveStudent(UUID transactionUUID, Student student);
}
