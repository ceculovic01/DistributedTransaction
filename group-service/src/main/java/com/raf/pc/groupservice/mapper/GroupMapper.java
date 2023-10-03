package com.raf.pc.groupservice.mapper;

import com.raf.pc.groupservice.dto.AddUpdateGroupDto;
import com.raf.pc.groupservice.dto.GroupDto;
import com.raf.pc.groupservice.dto.StudentDto;
import com.raf.pc.groupservice.model.Group;
import com.raf.pc.groupservice.model.Student;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class GroupMapper {

    public Group addUpdateDtoToModel(AddUpdateGroupDto addUpdateGroupDto) {
        Group group = new Group();
        group.setName(addUpdateGroupDto.getName());
        return group;
    }

    public GroupDto modelToDto(Group group) {
        GroupDto groupDto = new GroupDto();
        groupDto.setName(group.getName());
        groupDto.setId(group.getId());
        groupDto.setStudents(
                group.getStudents()
                        .stream()
                        .map(this::modelToStudentDto)
                        .collect(Collectors.toSet())
        );

        return groupDto;
    }

    public StudentDto modelToStudentDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setStudentUUID(student.getStudentUUID());

        return studentDto;
    }

    public Student addStudentDtoToModel(UUID studentUUID, Group group) {
        Student student = new Student();
        student.setStudentUUID(studentUUID);
        student.setGroup(group);

        return student;
    }
}
