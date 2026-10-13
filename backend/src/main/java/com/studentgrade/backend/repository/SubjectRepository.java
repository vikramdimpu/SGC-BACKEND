package com.studentgrade.backend.repository;

import com.studentgrade.backend.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByCode(String code);

    boolean existsByCode(String code);

    List<Subject> findBySemester(Integer semester);

    List<Subject> findByBranchAndSemester(String branch, Integer semester);

    List<Subject> findByBranch(String branch);
}
