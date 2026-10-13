package com.studentgrade.backend;

import com.studentgrade.backend.dto.MarksEntryDTO;
import com.studentgrade.backend.dto.StudentAcademicReportDTO;
import com.studentgrade.backend.model.SemesterResult;
import com.studentgrade.backend.model.Student;
import com.studentgrade.backend.model.Subject;
import com.studentgrade.backend.model.SubjectMarks;
import com.studentgrade.backend.repository.SemesterResultRepository;
import com.studentgrade.backend.repository.StudentRepository;
import com.studentgrade.backend.repository.SubjectMarksRepository;
import com.studentgrade.backend.repository.SubjectRepository;
import com.studentgrade.backend.service.AcademicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcademicCalculationTests {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private SubjectMarksRepository subjectMarksRepository;

    @Mock
    private SemesterResultRepository semesterResultRepository;

    private AcademicService academicService;

    @BeforeEach
    void setUp() {
        academicService = new AcademicService(
                studentRepository,
                subjectRepository,
                subjectMarksRepository,
                semesterResultRepository
        );
    }

    @Test
    void testEnterMarks_AllPassed_CorrectGradePoints() {
        Student student = new Student("Rahul", "21B91A0501", "rahul@test.edu", "CSE", "CSE", "2023-2027", 1, "A");
        Subject subject = new Subject("CS101", "Maths", "CSE", 1, "THEORY", 4.0, 30.0, 70.0, 0.0, 25.0, 40.0);

        when(studentRepository.findByRollNumber("21B91A0501")).thenReturn(Optional.of(student));
        when(subjectRepository.findByCode("CS101")).thenReturn(Optional.of(subject));
        when(subjectMarksRepository.findByStudentAndSubjectAndSemester(student, subject, 1)).thenReturn(Optional.empty());
        when(subjectMarksRepository.save(any(SubjectMarks.class))).thenAnswer(i -> i.getArgument(0));

        // Total 28 + 64 = 92/100 -> >=90% -> Grade O, 10.0 Grade Points
        MarksEntryDTO dto = new MarksEntryDTO("21B91A0501", "CS101", 1, 28.0, 64.0);
        SubjectMarks mark = academicService.enterMarks(dto);

        assertEquals("O", mark.getGrade());
        assertEquals(10.0, mark.getGradePoints());
        assertEquals("PASS", mark.getStatus());
        assertEquals(92.0, mark.getTotalMarks());
    }

    @Test
    void testEnterMarks_FailedExternalRequirement_ResultsInFGrade() {
        Student student = new Student("Rahul", "21B91A0501", "rahul@test.edu", "CSE", "CSE", "2023-2027", 1, "A");
        // Minimum external pass marks = 25.0
        Subject subject = new Subject("CS102", "Physics", "CSE", 1, "THEORY", 3.0, 30.0, 70.0, 0.0, 25.0, 40.0);

        when(studentRepository.findByRollNumber("21B91A0501")).thenReturn(Optional.of(student));
        when(subjectRepository.findByCode("CS102")).thenReturn(Optional.of(subject));
        when(subjectMarksRepository.findByStudentAndSubjectAndSemester(student, subject, 1)).thenReturn(Optional.empty());
        when(subjectMarksRepository.save(any(SubjectMarks.class))).thenAnswer(i -> i.getArgument(0));

        // External marks = 20.0 (< 25.0 min pass required), total = 50.0
        MarksEntryDTO dto = new MarksEntryDTO("21B91A0501", "CS102", 1, 30.0, 20.0);
        SubjectMarks mark = academicService.enterMarks(dto);

        // Despite 50% total, student failed the mandatory external exam requirement
        assertEquals("F", mark.getGrade());
        assertEquals(0.0, mark.getGradePoints());
        assertEquals("FAIL", mark.getStatus());
    }

    @Test
    void testCalculateSemesterResult_CreditWeightedSgpa() {
        Student student = new Student("Rahul", "21B91A0501", "rahul@test.edu", "CSE", "CSE", "2023-2027", 1, "A");

        Subject s1 = new Subject("CS101", "Maths", "CSE", 1, "THEORY", 4.0, 30.0, 70.0, 0.0, 25.0, 40.0);
        Subject s2 = new Subject("CS102", "Physics", "CSE", 1, "THEORY", 3.0, 30.0, 70.0, 0.0, 25.0, 40.0);
        Subject s3 = new Subject("CS105P", "C Lab", "CSE", 1, "LABORATORY", 1.5, 30.0, 70.0, 0.0, 25.0, 40.0);

        // s1: 4 credits, Grade O (10 GP) -> 40.0
        SubjectMarks m1 = new SubjectMarks(student, s1, 1, 28.0, 64.0, 92.0, 92.0, "O", 10.0, "PASS", true);
        // s2: 3 credits, Grade A (8 GP) -> 24.0
        SubjectMarks m2 = new SubjectMarks(student, s2, 1, 22.0, 50.0, 72.0, 72.0, "A", 8.0, "PASS", true);
        // s3: 1.5 credits, Grade A+ (9 GP) -> 13.5
        SubjectMarks m3 = new SubjectMarks(student, s3, 1, 26.0, 58.0, 84.0, 84.0, "A+", 9.0, "PASS", true);

        // Total Credits = 4 + 3 + 1.5 = 8.5
        // Weighted Points = 40 + 24 + 13.5 = 77.5
        // Expected SGPA = 77.5 / 8.5 = 9.12
        when(subjectMarksRepository.findByStudentAndSemester(student, 1)).thenReturn(List.of(m1, m2, m3));
        when(semesterResultRepository.findByStudentAndSemester(student, 1)).thenReturn(Optional.empty());
        when(semesterResultRepository.save(any(SemesterResult.class))).thenAnswer(i -> i.getArgument(0));

        SemesterResult result = academicService.calculateAndSaveSemesterResult(student, 1, true);

        assertNotNull(result);
        assertEquals(8.5, result.getRegisteredCredits());
        assertEquals(8.5, result.getEarnedCredits());
        assertEquals(9.12, result.getSgpa(), 0.01);
        assertEquals("PASS", result.getStatus());
        assertEquals(0, result.getBacklogCount());
    }

    @Test
    void testCumulativeCgpa_MultipleSemesters() {
        Student student = new Student("Rahul", "21B91A0501", "rahul@test.edu", "CSE", "CSE", "2023-2027", 2, "A");

        SemesterResult sem1 = new SemesterResult(student, 1, 20.0, 20.0, 8.5, "PASS", 0, true);
        SemesterResult sem2 = new SemesterResult(student, 2, 20.0, 20.0, 9.0, "PASS", 0, true);

        // Sem 1: 20 credits * 8.5 = 170
        // Sem 2: 20 credits * 9.0 = 180
        // Total: 350 / 40 credits = 8.75 CGPA
        when(semesterResultRepository.findByStudentAndPublishedTrueOrderBySemesterAsc(student))
                .thenReturn(List.of(sem1, sem2));
        when(subjectMarksRepository.findByStudentAndSemesterAndPublishedTrue(student, 1)).thenReturn(List.of());
        when(subjectMarksRepository.findByStudentAndSemesterAndPublishedTrue(student, 2)).thenReturn(List.of());

        StudentAcademicReportDTO report = academicService.getStudentAcademicReport(student, true);

        assertEquals(8.75, report.getCgpa(), 0.01);
        assertEquals(40.0, report.getTotalCreditsEarned());
        assertEquals(40.0, report.getTotalCreditsAttempted());
        assertEquals(0, report.getTotalBacklogs());
    }
}
