package com.studentgrade.backend.controller;

import com.studentgrade.backend.dto.StudentAcademicReportDTO;
import com.studentgrade.backend.exception.ResourceNotFoundException;
import com.studentgrade.backend.model.Student;
import com.studentgrade.backend.repository.StudentRepository;
import com.studentgrade.backend.service.AcademicService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
public class StudentAcademicController {

    private final StudentRepository studentRepository;
    private final AcademicService academicService;

    public StudentAcademicController(StudentRepository studentRepository,
                                     AcademicService academicService) {
        this.studentRepository = studentRepository;
        this.academicService = academicService;
    }

    @GetMapping("/me")
    public ResponseEntity<StudentAcademicReportDTO> getMyAcademicReport(Authentication authentication) {
        String rollNumber = authentication.getName();
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for account: " + rollNumber));

        // Returns only officially published semester results and marks for the student
        StudentAcademicReportDTO report = academicService.getStudentAcademicReport(student, true);
        return ResponseEntity.ok(report);
    }
}
