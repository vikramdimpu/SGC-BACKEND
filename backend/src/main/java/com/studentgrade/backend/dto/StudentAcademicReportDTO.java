package com.studentgrade.backend.dto;

import java.util.ArrayList;
import java.util.List;

public class StudentAcademicReportDTO {

    private Long studentId;
    private String rollNumber;
    private String name;
    private String email;
    private String department;
    private String branch;
    private String academicYear;
    private Integer currentSemester;
    private String section;
    private boolean active;

    private Double cgpa = 0.0;
    private Double totalCreditsEarned = 0.0;
    private Double totalCreditsAttempted = 0.0;
    private Integer totalBacklogs = 0;

    private List<SemesterAcademicSummaryDTO> semesters = new ArrayList<>();

    public StudentAcademicReportDTO() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public Double getTotalCreditsEarned() {
        return totalCreditsEarned;
    }

    public void setTotalCreditsEarned(Double totalCreditsEarned) {
        this.totalCreditsEarned = totalCreditsEarned;
    }

    public Double getTotalCreditsAttempted() {
        return totalCreditsAttempted;
    }

    public void setTotalCreditsAttempted(Double totalCreditsAttempted) {
        this.totalCreditsAttempted = totalCreditsAttempted;
    }

    public Integer getTotalBacklogs() {
        return totalBacklogs;
    }

    public void setTotalBacklogs(Integer totalBacklogs) {
        this.totalBacklogs = totalBacklogs;
    }

    public List<SemesterAcademicSummaryDTO> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<SemesterAcademicSummaryDTO> semesters) {
        this.semesters = semesters;
    }
}
