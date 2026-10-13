package com.studentgrade.backend.service;

import com.studentgrade.backend.dto.StudentRequestDTO;
import com.studentgrade.backend.model.Student;

import java.util.List;

public interface StudentService {

    List<Student> getAllStudents();

    Student getStudentById(Long id);

    Student createStudent(StudentRequestDTO request);

    Student updateStudent(Long id, StudentRequestDTO request);

    void deleteStudent(Long id);
}
