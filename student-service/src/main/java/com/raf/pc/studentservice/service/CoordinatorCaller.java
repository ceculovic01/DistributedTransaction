package com.raf.pc.studentservice.service;

import com.raf.pc.studentservice.config.ServicesConfig;
import com.raf.pc.studentservice.dto.*;
import com.raf.pc.studentservice.mapper.StudentMapper;
import com.raf.pc.studentservice.model.Student;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CoordinatorCaller {

    private final ServicesConfig servicesConfig;
    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;
    private final StudentMapper studentMapper;

    public CoordinatorCaller(ServicesConfig servicesConfig, RestTemplate restTemplate,
                             RetryTemplate retryTemplate, StudentMapper studentMapper) {
        this.servicesConfig = servicesConfig;
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
        this.studentMapper = studentMapper;
    }

    public void createTransaction(CreateStudentDto createStudentDto, Student student) {
        CreateTransactionDto createTransactionDto = new CreateTransactionDto();

        List<ServiceParticipantDto> serviceParticipantDtoList = new ArrayList<>();

        ServiceParticipantDto studentParticipant = new ServiceParticipantDto();
        studentParticipant.setName(servicesConfig.studentServiceName);
        studentParticipant.setUrl(servicesConfig.studentServiceAddStudentURL);
        studentParticipant.setHttpMethod(HttpMethod.POST.toString());
        studentParticipant.setBody(student);
        serviceParticipantDtoList.add(studentParticipant);

        ServiceParticipantDto managementParticipant = new ServiceParticipantDto();
        AddStudentToYearDto addStudentToYearDto = new AddStudentToYearDto(student.getStudentUUID(),
                createStudentDto.getYear(), createStudentDto.getSubjects());
        managementParticipant.setName(servicesConfig.managementServiceName);
        managementParticipant.setUrl(servicesConfig.managementServiceAddStudentURL);
        managementParticipant.setHttpMethod(HttpMethod.POST.toString());
        managementParticipant.setBody(addStudentToYearDto);
        serviceParticipantDtoList.add(managementParticipant);

        ServiceParticipantDto groupParticipant = new ServiceParticipantDto();
        AddStudentToGroupDto addStudentToGroupDto = new AddStudentToGroupDto(student.getStudentUUID(),
                createStudentDto.getGroupName());
        groupParticipant.setName(servicesConfig.groupServiceName);
        groupParticipant.setHttpMethod(HttpMethod.POST.toString());
        groupParticipant.setUrl(servicesConfig.groupServiceAddStudentURL);
        groupParticipant.setBody(addStudentToGroupDto);
        serviceParticipantDtoList.add(groupParticipant);

        createTransactionDto.setServiceParticipantDtoList(serviceParticipantDtoList);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("description", "Adding new student to group and year with subjects");
        metadata.put("groupDto", addStudentToGroupDto);
        metadata.put("managementDto", addStudentToYearDto);
        metadata.put("studentDto", studentMapper.modelToDto(student));
        createTransactionDto.setMetadata(metadata);

        HttpEntity<CreateTransactionDto> httpEntity = new HttpEntity<>(createTransactionDto);
        retryTemplate.execute(context -> restTemplate.exchange(servicesConfig.coordinatorURL,
                HttpMethod.POST, httpEntity, CreateTransactionDto.class));
    }
}
