package com.raf.pc.studentmanagement.repository;

import com.raf.pc.studentmanagement.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByName(String name);

    @Query(value = "select s from subject s where s.name in :subjects")
    Set<Subject> getSubjectsInList(@Param(value = "subjects") List<String> subjects);
}
