package com.studentgrade.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 30)
    private String rollNumber;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String department = "Computer Science & Engineering";

    @Column(length = 50)
    private String branch = "CSE";

    @Column(length = 20)
    private String academicYear = "2023-2027";

    @Column(nullable = false)
    private Integer currentSemester = 1;

    @Column(length = 10)
    private String section = "A";

    @Column(nullable = false)
    private boolean active = true;

    // Previous legacy 5-subject columns preserved for compatibility
    private Double subject1 = 0.0;
    private Double subject2 = 0.0;
    private Double subject3 = 0.0;
    private Double subject4 = 0.0;
    private Double subject5 = 0.0;

    private Double totalMarks = 0.0;
    private Double percentage = 0.0;

    @Column(length = 10)
    private String grade = "N/A";

    @Column(length = 10)
    private String status = "PENDING";

    public Student() {
    }

    public Student(String name, String rollNumber, String email, String department, String branch,
                   String academicYear, Integer currentSemester, String section) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.email = email;
        this.department = department;
        this.branch = branch;
        this.academicYear = academicYear;
        this.currentSemester = currentSemester;
        this.section = section;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public Integer getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(Integer currentSemester) {
        this.currentSemester = currentSemester;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Double getSubject1() {
        return subject1;
    }

    public void setSubject1(Double subject1) {
        this.subject1 = subject1;
    }

    public Double getSubject2() {
        return subject2;
    }

    public void setSubject2(Double subject2) {
        this.subject2 = subject2;
    }

    public Double getSubject3() {
        return subject3;
    }

    public void setSubject3(Double subject3) {
        this.subject3 = subject3;
    }

    public Double getSubject4() {
        return subject4;
    }

    public void setSubject4(Double subject4) {
        this.subject4 = subject4;
    }

    public Double getSubject5() {
        return subject5;
    }

    public void setSubject5(Double subject5) {
        this.subject5 = subject5;
    }

    public Double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
