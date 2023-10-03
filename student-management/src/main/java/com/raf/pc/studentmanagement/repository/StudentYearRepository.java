package com.raf.pc.studentmanagement.repository;

import com.raf.pc.studentmanagement.model.StudentYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentYearRepository extends JpaRepository<StudentYear, Long> {

    Optional<StudentYear> findByYear(Integer year);

}
