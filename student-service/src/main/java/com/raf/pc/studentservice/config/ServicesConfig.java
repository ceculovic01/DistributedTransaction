package com.raf.pc.studentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

    public String groupServiceName;
    public String managementServiceName;
    public String studentServiceName;
    public String groupServiceAddStudentURL;
    public String managementServiceAddStudentURL;
    public String studentServiceAddStudentURL;
    public String coordinatorURL;

    public ServicesConfig(@Value("${services.group.name}") String groupServiceName,
                          @Value("${services.management.name}") String managementServiceName,
                          @Value("${spring.application.name}") String studentServiceName,
                          @Value("${services.group.add-student-url}") String groupServiceAddStudentURL,
                          @Value("${services.management.add-student-url}") String managementServiceAddStudentURL,
                          @Value("${services.student.add-student-url}") String studentServiceAddStudentURL,
                          @Value("${services.coordinator.url}") String coordinatorURL) {
        this.groupServiceName = groupServiceName;
        this.managementServiceName = managementServiceName;
        this.studentServiceName = studentServiceName;
        this.groupServiceAddStudentURL = groupServiceAddStudentURL;
        this.managementServiceAddStudentURL = managementServiceAddStudentURL;
        this.studentServiceAddStudentURL = studentServiceAddStudentURL;
        this.coordinatorURL = coordinatorURL;
    }
}
