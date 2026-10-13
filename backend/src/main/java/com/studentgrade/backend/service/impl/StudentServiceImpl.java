package com.studentgrade.backend.service.impl;

import com.studentgrade.backend.dto.StudentRequestDTO;
import com.studentgrade.backend.exception.DuplicateResourceException;
import com.studentgrade.backend.exception.ResourceNotFoundException;
import com.studentgrade.backend.model.Student;
import com.studentgrade.backend.repository.StudentRepository;
import com.studentgrade.backend.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    @Override
    public Student createStudent(StudentRequestDTO request) {
        String cleanRollNumber = request.getRollNumber().trim();

        if (studentRepository.existsByRollNumber(cleanRollNumber)) {
            throw new DuplicateResourceException("A student with roll number '" + cleanRollNumber + "' already exists.");
        }

        Student student = new Student();
        populateCalculatedStudent(student, request);

        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Long id, StudentRequestDTO request) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        String cleanRollNumber = request.getRollNumber().trim();
        if (studentRepository.existsByRollNumberAndIdNot(cleanRollNumber, id)) {
            throw new DuplicateResourceException("Roll number '" + cleanRollNumber + "' is already assigned to another student.");
        }

        populateCalculatedStudent(existingStudent, request);

        return studentRepository.save(existingStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        studentRepository.delete(existingStudent);
    }

    private void populateCalculatedStudent(Student student, StudentRequestDTO request) {
        double s1 = request.getSubject1();
        double s2 = request.getSubject2();
        double s3 = request.getSubject3();
        double s4 = request.getSubject4();
        double s5 = request.getSubject5();

        double total = s1 + s2 + s3 + s4 + s5;
        double rawPercentage = (total / 500.0) * 100.0;
        double percentage = Math.round(rawPercentage * 100.0) / 100.0;

        String grade;
        if (percentage >= 90.0) {
            grade = "A";
        } else if (percentage >= 75.0) {
            grade = "B";
        } else if (percentage >= 60.0) {
            grade = "C";
        } else if (percentage >= 40.0) {
            grade = "D";
        } else {
            grade = "F";
        }

        String status = "F".equals(grade) ? "FAIL" : "PASS";

        student.setName(request.getName().trim());
        student.setRollNumber(request.getRollNumber().trim());
        student.setSubject1(s1);
        student.setSubject2(s2);
        student.setSubject3(s3);
        student.setSubject4(s4);
        student.setSubject5(s5);
        student.setTotalMarks(total);
        student.setPercentage(percentage);
        student.setGrade(grade);
        student.setStatus(status);
    }
}
