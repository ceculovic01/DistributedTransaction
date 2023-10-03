package com.raf.pc.groupservice.controller;

import com.raf.pc.groupservice.dto.AddStudentDto;
import com.raf.pc.groupservice.dto.AddUpdateGroupDto;
import com.raf.pc.groupservice.dto.GroupDto;
import com.raf.pc.groupservice.dto.StudentDto;
import com.raf.pc.groupservice.model.Student;
import com.raf.pc.groupservice.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/groups")
public class GroupController {

    private final GroupService groupService;


    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<GroupDto>> findAllGroups() {
        return ResponseEntity.ok(groupService.findAllGroups());
    }

    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@RequestBody @Valid AddUpdateGroupDto groupDto) {
        return ResponseEntity.ok(groupService.createGroup(groupDto));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupDto> findGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.findGroupById(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<GroupDto> deleteGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.removeGroup(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<GroupDto> updateGroupById(@PathVariable Long id, @RequestBody @Valid AddUpdateGroupDto groupDto) {
        return ResponseEntity.ok(groupService.updateGroup(id, groupDto));
    }

    @PostMapping(value = "/student")
    public ResponseEntity<?> addStudentToGroup(@RequestHeader("txn-header") String header,
                                               @RequestBody @Valid AddStudentDto addStudentDto) {
        UUID uuid = UUID.fromString(header);
        Student student = groupService.validateStudentForGroup(addStudentDto);
        groupService.saveStudent(uuid, student);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/student/{uuid}")
    public ResponseEntity<StudentDto> removeStudentFromGroup(@PathVariable UUID uuid) {
        return ResponseEntity.ok(groupService.removeStudentFromGroup(uuid));
    }

}
