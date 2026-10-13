package com.studentgrade.backend.service;

import com.studentgrade.backend.dto.MarksEntryDTO;
import com.studentgrade.backend.dto.SemesterAcademicSummaryDTO;
import com.studentgrade.backend.dto.StudentAcademicReportDTO;
import com.studentgrade.backend.dto.SubjectMarkDetailDTO;
import com.studentgrade.backend.exception.ResourceNotFoundException;
import com.studentgrade.backend.model.SemesterResult;
import com.studentgrade.backend.model.Student;
import com.studentgrade.backend.model.Subject;
import com.studentgrade.backend.model.SubjectMarks;
import com.studentgrade.backend.repository.SemesterResultRepository;
import com.studentgrade.backend.repository.StudentRepository;
import com.studentgrade.backend.repository.SubjectMarksRepository;
import com.studentgrade.backend.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AcademicService {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectMarksRepository subjectMarksRepository;
    private final SemesterResultRepository semesterResultRepository;

    public AcademicService(StudentRepository studentRepository,
                           SubjectRepository subjectRepository,
                           SubjectMarksRepository subjectMarksRepository,
                           SemesterResultRepository semesterResultRepository) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.subjectMarksRepository = subjectMarksRepository;
        this.semesterResultRepository = semesterResultRepository;
    }

    public SubjectMarks enterMarks(MarksEntryDTO dto) {
        Student student = studentRepository.findByRollNumber(dto.getRollNumber().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with roll number: " + dto.getRollNumber()));

        Subject subject = subjectRepository.findByCode(dto.getSubjectCode().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with code: " + dto.getSubjectCode()));

        if (dto.getInternalMarks() > subject.getInternalMaxMarks()) {
            throw new IllegalArgumentException(String.format("Internal marks (%.1f) cannot exceed maximum allowed (%.1f) for %s",
                    dto.getInternalMarks(), subject.getInternalMaxMarks(), subject.getCode()));
        }

        if (dto.getExternalMarks() > subject.getExternalMaxMarks()) {
            throw new IllegalArgumentException(String.format("External marks (%.1f) cannot exceed maximum allowed (%.1f) for %s",
                    dto.getExternalMarks(), subject.getExternalMaxMarks(), subject.getCode()));
        }

        double internal = dto.getInternalMarks();
        double external = dto.getExternalMarks();
        double total = internal + external;
        double maxTotal = subject.getInternalMaxMarks() + subject.getExternalMaxMarks();
        double percentage = maxTotal > 0 ? Math.round(((total / maxTotal) * 100.0) * 100.0) / 100.0 : 0.0;

        // Passing criteria check
        boolean passedInternal = internal >= subject.getInternalMinPassMarks();
        boolean passedExternal = external >= subject.getExternalMinPassMarks();
        boolean passedTotal = total >= subject.getTotalMinPassMarks();

        String grade;
        double gradePoints;
        String status;

        if (!passedInternal || !passedExternal || !passedTotal) {
            grade = "F";
            gradePoints = 0.0;
            status = "FAIL";
        } else {
            status = "PASS";
            if (percentage >= 90.0) {
                grade = "O";
                gradePoints = 10.0;
            } else if (percentage >= 80.0) {
                grade = "A+";
                gradePoints = 9.0;
            } else if (percentage >= 70.0) {
                grade = "A";
                gradePoints = 8.0;
            } else if (percentage >= 60.0) {
                grade = "B+";
                gradePoints = 7.0;
            } else if (percentage >= 55.0) {
                grade = "B";
                gradePoints = 6.0;
            } else if (percentage >= 50.0) {
                grade = "C";
                gradePoints = 5.0;
            } else if (percentage >= 40.0) {
                grade = "P";
                gradePoints = 4.0;
            } else {
                grade = "F";
                gradePoints = 0.0;
                status = "FAIL";
            }
        }

        Optional<SubjectMarks> existing = subjectMarksRepository.findByStudentAndSubjectAndSemester(student, subject, dto.getSemester());
        SubjectMarks mark = existing.orElseGet(SubjectMarks::new);

        mark.setStudent(student);
        mark.setSubject(subject);
        mark.setSemester(dto.getSemester());
        mark.setInternalMarks(internal);
        mark.setExternalMarks(external);
        mark.setTotalMarks(total);
        mark.setPercentage(percentage);
        mark.setGrade(grade);
        mark.setGradePoints(gradePoints);
        mark.setStatus(status);

        return subjectMarksRepository.save(mark);
    }

    public void publishStudentSemester(Long studentId, Integer semester) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        List<SubjectMarks> marksList = subjectMarksRepository.findByStudentAndSemester(student, semester);
        for (SubjectMarks mark : marksList) {
            mark.setPublished(true);
            subjectMarksRepository.save(mark);
        }

        calculateAndSaveSemesterResult(student, semester, true);
    }

    public void publishSemesterForAll(Integer semester) {
        List<SubjectMarks> marksList = subjectMarksRepository.findBySemester(semester);
        for (SubjectMarks mark : marksList) {
            mark.setPublished(true);
            subjectMarksRepository.save(mark);
        }

        List<Student> students = studentRepository.findAll();
        for (Student student : students) {
            List<SubjectMarks> studentMarks = subjectMarksRepository.findByStudentAndSemester(student, semester);
            if (!studentMarks.isEmpty()) {
                calculateAndSaveSemesterResult(student, semester, true);
            }
        }
    }

    public SemesterResult calculateAndSaveSemesterResult(Student student, Integer semester, boolean published) {
        List<SubjectMarks> marksList = subjectMarksRepository.findByStudentAndSemester(student, semester);
        if (marksList.isEmpty()) {
            return null;
        }

        double totalRegisteredCredits = 0.0;
        double totalEarnedCredits = 0.0;
        double totalWeightedGradePoints = 0.0;
        int backlogs = 0;

        for (SubjectMarks sm : marksList) {
            double credits = sm.getSubject().getCredits();
            totalRegisteredCredits += credits;

            if ("PASS".equalsIgnoreCase(sm.getStatus())) {
                totalEarnedCredits += credits;
                totalWeightedGradePoints += (credits * sm.getGradePoints());
            } else {
                backlogs++;
            }
        }

        double sgpa = totalRegisteredCredits > 0
                ? Math.round((totalWeightedGradePoints / totalRegisteredCredits) * 100.0) / 100.0
                : 0.0;

        String status = backlogs == 0 ? "PASS" : "FAIL";

        Optional<SemesterResult> existing = semesterResultRepository.findByStudentAndSemester(student, semester);
        SemesterResult result = existing.orElseGet(SemesterResult::new);

        result.setStudent(student);
        result.setSemester(semester);
        result.setRegisteredCredits(totalRegisteredCredits);
        result.setEarnedCredits(totalEarnedCredits);
        result.setSgpa(sgpa);
        result.setStatus(status);
        result.setBacklogCount(backlogs);
        result.setPublished(published);

        return semesterResultRepository.save(result);
    }

    @Transactional(readOnly = true)
    public StudentAcademicReportDTO getStudentAcademicReport(Student student, boolean onlyPublished) {
        StudentAcademicReportDTO report = new StudentAcademicReportDTO();
        report.setStudentId(student.getId());
        report.setRollNumber(student.getRollNumber());
        report.setName(student.getName());
        report.setEmail(student.getEmail());
        report.setDepartment(student.getDepartment());
        report.setBranch(student.getBranch());
        report.setAcademicYear(student.getAcademicYear());
        report.setCurrentSemester(student.getCurrentSemester());
        report.setSection(student.getSection());
        report.setActive(student.isActive());

        List<SemesterResult> semesterResults = onlyPublished
                ? semesterResultRepository.findByStudentAndPublishedTrueOrderBySemesterAsc(student)
                : semesterResultRepository.findByStudentOrderBySemesterAsc(student);

        double cumulativeWeightedPoints = 0.0;
        double cumulativeRegisteredCredits = 0.0;
        double totalCreditsEarned = 0.0;
        double totalCreditsAttempted = 0.0;
        int activeBacklogs = 0;

        List<SemesterAcademicSummaryDTO> semesterSummaries = new ArrayList<>();

        for (SemesterResult sr : semesterResults) {
            SemesterAcademicSummaryDTO summary = new SemesterAcademicSummaryDTO();
            summary.setSemester(sr.getSemester());
            summary.setRegisteredCredits(sr.getRegisteredCredits());
            summary.setEarnedCredits(sr.getEarnedCredits());
            summary.setSgpa(sr.getSgpa());
            summary.setStatus(sr.getStatus());
            summary.setBacklogCount(sr.getBacklogCount());
            summary.setPublished(sr.isPublished());

            List<SubjectMarks> marksList = onlyPublished
                    ? subjectMarksRepository.findByStudentAndSemesterAndPublishedTrue(student, sr.getSemester())
                    : subjectMarksRepository.findByStudentAndSemester(student, sr.getSemester());

            List<SubjectMarkDetailDTO> detailDTOs = new ArrayList<>();
            for (SubjectMarks sm : marksList) {
                SubjectMarkDetailDTO detail = new SubjectMarkDetailDTO();
                detail.setId(sm.getId());
                detail.setSubjectCode(sm.getSubject().getCode());
                detail.setSubjectName(sm.getSubject().getName());
                detail.setType(sm.getSubject().getType());
                detail.setCredits(sm.getSubject().getCredits());
                detail.setInternalMarks(sm.getInternalMarks());
                detail.setInternalMax(sm.getSubject().getInternalMaxMarks());
                detail.setExternalMarks(sm.getExternalMarks());
                detail.setExternalMax(sm.getSubject().getExternalMaxMarks());
                detail.setTotalMarks(sm.getTotalMarks());
                detail.setTotalMax(sm.getSubject().getInternalMaxMarks() + sm.getSubject().getExternalMaxMarks());
                detail.setPercentage(sm.getPercentage());
                detail.setGrade(sm.getGrade());
                detail.setGradePoints(sm.getGradePoints());
                detail.setStatus(sm.getStatus());
                detail.setPublished(sm.isPublished());
                detailDTOs.add(detail);
            }
            summary.setSubjects(detailDTOs);
            semesterSummaries.add(summary);

            cumulativeRegisteredCredits += sr.getRegisteredCredits();
            cumulativeWeightedPoints += (sr.getRegisteredCredits() * sr.getSgpa());
            totalCreditsEarned += sr.getEarnedCredits();
            totalCreditsAttempted += sr.getRegisteredCredits();
            activeBacklogs += sr.getBacklogCount();
        }

        double cgpa = cumulativeRegisteredCredits > 0
                ? Math.round((cumulativeWeightedPoints / cumulativeRegisteredCredits) * 100.0) / 100.0
                : 0.0;

        report.setCgpa(cgpa);
        report.setTotalCreditsEarned(totalCreditsEarned);
        report.setTotalCreditsAttempted(totalCreditsAttempted);
        report.setTotalBacklogs(activeBacklogs);
        report.setSemesters(semesterSummaries);

        return report;
    }
}
