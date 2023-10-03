package com.raf.pc.studentmanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "student_year")
public class StudentYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer year;

    @OneToMany(mappedBy = "studentYear", fetch = FetchType.EAGER)
    private Set<Student> students = new HashSet<>();

}
