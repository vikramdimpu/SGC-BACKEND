package com.studentgrade.backend.controller;

import com.studentgrade.backend.dto.*;
import com.studentgrade.backend.exception.ResourceNotFoundException;
import com.studentgrade.backend.model.Student;
import com.studentgrade.backend.model.Subject;
import com.studentgrade.backend.model.SubjectMarks;
import com.studentgrade.backend.repository.StudentRepository;
import com.studentgrade.backend.service.AcademicService;
import com.studentgrade.backend.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAcademicController {

    private final AdminService adminService;
    private final AcademicService academicService;
    private final StudentRepository studentRepository;

    public AdminAcademicController(AdminService adminService,
                                   AcademicService academicService,
                                   StudentRepository studentRepository) {
        this.adminService = adminService;
        this.academicService = academicService;
        this.studentRepository = studentRepository;
    }

    // Student Management
    @PostMapping("/students")
    public ResponseEntity<Student> registerStudent(@Valid @RequestBody StudentRegisterRequest request) {
        Student created = adminService.registerStudent(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) Integer semester) {
        List<Student> students = adminService.getAllStudents(search, branch, semester);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return ResponseEntity.ok(student);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id,
                                                 @Valid @RequestBody StudentUpdateRequest request) {
        Student updated = adminService.updateStudent(id, request);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/students/{id}/status")
    public ResponseEntity<Student> toggleStudentStatus(@PathVariable Long id) {
        Student updated = adminService.toggleStudentStatus(id);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/students/{id}/reset-password")
    public ResponseEntity<Map<String, String>> resetStudentPassword(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String newPassword = body != null ? body.get("newPassword") : "Student@123";
        adminService.resetStudentPassword(id, newPassword);
        return ResponseEntity.ok(Map.of("message", "Student password successfully reset."));
    }

    @GetMapping("/students/{id}/results")
    public ResponseEntity<StudentAcademicReportDTO> getStudentAcademicReport(@PathVariable Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        // Admin can inspect both draft and published results
        StudentAcademicReportDTO report = academicService.getStudentAcademicReport(student, false);
        return ResponseEntity.ok(report);
    }

    // Subject Management
    @PostMapping("/subjects")
    public ResponseEntity<Subject> createSubject(@Valid @RequestBody SubjectDTO dto) {
        Subject created = adminService.createSubject(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = adminService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/subjects/filter")
    public ResponseEntity<List<Subject>> getSubjectsByBranchAndSemester(
            @RequestParam String branch,
            @RequestParam Integer semester) {
        List<Subject> subjects = adminService.getSubjectsByBranchAndSemester(branch, semester);
        return ResponseEntity.ok(subjects);
    }

    // Marks Management & Publication
    @PostMapping("/marks")
    public ResponseEntity<SubjectMarks> enterMarks(@Valid @RequestBody MarksEntryDTO dto) {
        SubjectMarks mark = academicService.enterMarks(dto);
        return ResponseEntity.ok(mark);
    }

    @PostMapping("/marks/bulk-csv")
    public ResponseEntity<List<String>> processBulkMarksCsv(@RequestBody Map<String, String> payload) {
        String csvContent = payload.getOrDefault("csv", "");
        List<String> results = adminService.processBulkMarksCsv(csvContent);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/results/publish")
    public ResponseEntity<Map<String, String>> publishSemesterResults(@RequestParam Integer semester) {
        academicService.publishSemesterForAll(semester);
        return ResponseEntity.ok(Map.of("message", "Semester " + semester + " results published successfully for all enrolled students."));
    }

    @PostMapping("/students/{id}/publish-semester/{semester}")
    public ResponseEntity<Map<String, String>> publishStudentSemester(
            @PathVariable Long id,
            @PathVariable Integer semester) {
        academicService.publishStudentSemester(id, semester);
        return ResponseEntity.ok(Map.of("message", "Semester " + semester + " results published successfully for student ID " + id + "."));
    }
}
