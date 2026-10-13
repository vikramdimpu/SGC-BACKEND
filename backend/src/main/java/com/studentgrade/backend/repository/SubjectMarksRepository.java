package com.studentgrade.backend.repository;

import com.studentgrade.backend.model.Student;
import com.studentgrade.backend.model.Subject;
import com.studentgrade.backend.model.SubjectMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectMarksRepository extends JpaRepository<SubjectMarks, Long> {

    List<SubjectMarks> findByStudentAndSemester(Student student, Integer semester);

    List<SubjectMarks> findByStudentAndSemesterAndPublishedTrue(Student student, Integer semester);

    List<SubjectMarks> findByStudentAndPublishedTrue(Student student);

    List<SubjectMarks> findByStudent(Student student);

    Optional<SubjectMarks> findByStudentAndSubjectAndSemester(Student student, Subject subject, Integer semester);

    List<SubjectMarks> findBySemester(Integer semester);
}
