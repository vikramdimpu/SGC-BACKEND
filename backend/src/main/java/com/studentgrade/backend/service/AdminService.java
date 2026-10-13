package com.studentgrade.backend.service;

import com.studentgrade.backend.dto.MarksEntryDTO;
import com.studentgrade.backend.dto.StudentRegisterRequest;
import com.studentgrade.backend.dto.StudentUpdateRequest;
import com.studentgrade.backend.dto.SubjectDTO;
import com.studentgrade.backend.exception.DuplicateResourceException;
import com.studentgrade.backend.exception.ResourceNotFoundException;
import com.studentgrade.backend.model.Role;
import com.studentgrade.backend.model.Student;
import com.studentgrade.backend.model.Subject;
import com.studentgrade.backend.model.User;
import com.studentgrade.backend.repository.StudentRepository;
import com.studentgrade.backend.repository.SubjectRepository;
import com.studentgrade.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final PasswordEncoder passwordEncoder;
    private final AcademicService academicService;

    @Value("${app.academic.roll-number-regex:^[A-Za-z0-9\\-_/]{4,20}$}")
    private String rollNumberRegex;

    public AdminService(StudentRepository studentRepository,
                        UserRepository userRepository,
                        SubjectRepository subjectRepository,
                        PasswordEncoder passwordEncoder,
                        AcademicService academicService) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.passwordEncoder = passwordEncoder;
        this.academicService = academicService;
    }

    public Student registerStudent(StudentRegisterRequest request) {
        String roll = request.getRollNumber().trim();

        // Validate roll number with configurable regex
        if (!Pattern.matches(rollNumberRegex, roll)) {
            throw new IllegalArgumentException(
                    "Roll number '" + roll + "' does not match college enrollment ID format (allowed pattern: " + rollNumberRegex + ")");
        }

        if (studentRepository.existsByRollNumber(roll) || userRepository.existsByUsername(roll)) {
            throw new DuplicateResourceException("Student with roll number '" + roll + "' already exists.");
        }

        String rawPassword = (request.getInitialPassword() != null && !request.getInitialPassword().isBlank())
                ? request.getInitialPassword().trim()
                : "Student@123";

        User user = new User(roll, passwordEncoder.encode(rawPassword), Role.ROLE_STUDENT, true);
        userRepository.save(user);

        Student student = new Student(
                request.getName().trim(),
                roll,
                request.getEmail() != null ? request.getEmail().trim() : null,
                request.getDepartment().trim(),
                request.getBranch().trim().toUpperCase(),
                request.getAcademicYear().trim(),
                request.getCurrentSemester(),
                request.getSection() != null ? request.getSection().trim().toUpperCase() : "A"
        );

        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, StudentUpdateRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        student.setName(request.getName().trim());
        student.setEmail(request.getEmail() != null ? request.getEmail().trim() : null);
        student.setDepartment(request.getDepartment().trim());
        student.setBranch(request.getBranch().trim().toUpperCase());
        student.setAcademicYear(request.getAcademicYear().trim());
        student.setCurrentSemester(request.getCurrentSemester());
        student.setSection(request.getSection() != null ? request.getSection().trim().toUpperCase() : "A");
        student.setActive(request.isActive());

        // Sync with user account active flag
        userRepository.findByUsername(student.getRollNumber()).ifPresent(u -> {
            u.setActive(request.isActive());
            userRepository.save(u);
        });

        return studentRepository.save(student);
    }

    public Student toggleStudentStatus(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        boolean newStatus = !student.isActive();
        student.setActive(newStatus);

        userRepository.findByUsername(student.getRollNumber()).ifPresent(u -> {
            u.setActive(newStatus);
            userRepository.save(u);
        });

        return studentRepository.save(student);
    }

    public void resetStudentPassword(Long id, String newPassword) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        User user = userRepository.findByUsername(student.getRollNumber())
                .orElseThrow(() -> new ResourceNotFoundException("User account not found for roll number: " + student.getRollNumber()));

        String passToSet = (newPassword != null && !newPassword.isBlank()) ? newPassword : "Student@123";
        user.setPassword(passwordEncoder.encode(passToSet));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<Student> getAllStudents(String search, String branch, Integer semester) {
        List<Student> students = studentRepository.findAll();

        return students.stream()
                .filter(s -> {
                    boolean matchSearch = (search == null || search.isBlank()) ||
                            s.getName().toLowerCase().contains(search.toLowerCase()) ||
                            s.getRollNumber().toLowerCase().contains(search.toLowerCase());

                    boolean matchBranch = (branch == null || branch.isBlank() || "ALL".equalsIgnoreCase(branch)) ||
                            s.getBranch().equalsIgnoreCase(branch);

                    boolean matchSem = (semester == null || semester == 0) ||
                            s.getCurrentSemester().equals(semester);

                    return matchSearch && matchBranch && matchSem;
                })
                .collect(Collectors.toList());
    }

    public Subject createSubject(SubjectDTO dto) {
        String code = dto.getCode().trim().toUpperCase();
        if (subjectRepository.existsByCode(code)) {
            throw new DuplicateResourceException("Subject code '" + code + "' already exists.");
        }

        Subject subject = new Subject(
                code,
                dto.getName().trim(),
                dto.getBranch().trim().toUpperCase(),
                dto.getSemester(),
                dto.getType().trim().toUpperCase(),
                dto.getCredits(),
                dto.getInternalMaxMarks(),
                dto.getExternalMaxMarks(),
                dto.getInternalMinPassMarks(),
                dto.getExternalMinPassMarks(),
                dto.getTotalMinPassMarks()
        );

        return subjectRepository.save(subject);
    }

    @Transactional(readOnly = true)
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Subject> getSubjectsByBranchAndSemester(String branch, Integer semester) {
        return subjectRepository.findByBranchAndSemester(branch.toUpperCase(), semester);
    }

    public List<String> processBulkMarksCsv(String csvContent) {
        List<String> logs = new ArrayList<>();
        String[] lines = csvContent.split("\\r?\\n");

        int processed = 0;
        int failed = 0;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty() || line.startsWith("#") || (i == 0 && line.toLowerCase().contains("roll"))) {
                continue; // Skip header / comments / empty lines
            }

            String[] tokens = line.split(",");
            if (tokens.length < 5) {
                logs.add("Line " + (i + 1) + ": Invalid format (expected: RollNumber,SubjectCode,Semester,Internal,External)");
                failed++;
                continue;
            }

            try {
                String roll = tokens[0].trim();
                String code = tokens[1].trim();
                int semester = Integer.parseInt(tokens[2].trim());
                double internal = Double.parseDouble(tokens[3].trim());
                double external = Double.parseDouble(tokens[4].trim());

                MarksEntryDTO dto = new MarksEntryDTO(roll, code, semester, internal, external);
                academicService.enterMarks(dto);
                processed++;
            } catch (Exception ex) {
                failed++;
                logs.add("Line " + (i + 1) + ": " + ex.getMessage());
            }
        }

        logs.add(0, String.format("CSV Processing Complete: %d successfully saved, %d failed.", processed, failed));
        return logs;
    }
}
