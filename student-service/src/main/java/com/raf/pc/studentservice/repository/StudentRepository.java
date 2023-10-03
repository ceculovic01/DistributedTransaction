package com.raf.pc.studentservice.repository;

import com.raf.pc.studentservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByIndex(String index);
    Optional<Student> findByStudentUUID(UUID studentUUID);
}
