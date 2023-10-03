package com.raf.pc.groupservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_uuid", nullable = false)
    private UUID studentUUID;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
}
