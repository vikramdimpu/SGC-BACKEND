package com.studentgrade.backend.repository;

import com.studentgrade.backend.model.SemesterResult;
import com.studentgrade.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterResultRepository extends JpaRepository<SemesterResult, Long> {

    Optional<SemesterResult> findByStudentAndSemester(Student student, Integer semester);

    List<SemesterResult> findByStudentOrderBySemesterAsc(Student student);

    List<SemesterResult> findByStudentAndPublishedTrueOrderBySemesterAsc(Student student);

    List<SemesterResult> findBySemester(Integer semester);
}
