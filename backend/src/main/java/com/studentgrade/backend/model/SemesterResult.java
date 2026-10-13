package com.studentgrade.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "semester_results", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "semester"})
})
public class SemesterResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false)
    private Double registeredCredits = 0.0;

    @Column(nullable = false)
    private Double earnedCredits = 0.0;

    @Column(nullable = false)
    private Double sgpa = 0.0;

    @Column(nullable = false, length = 15)
    private String status = "PENDING"; // PASS, FAIL, PENDING

    @Column(nullable = false)
    private Integer backlogCount = 0;

    @Column(nullable = false)
    private boolean published = false;

    public SemesterResult() {
    }

    public SemesterResult(Student student, Integer semester, Double registeredCredits,
                          Double earnedCredits, Double sgpa, String status,
                          Integer backlogCount, boolean published) {
        this.student = student;
        this.semester = semester;
        this.registeredCredits = registeredCredits;
        this.earnedCredits = earnedCredits;
        this.sgpa = sgpa;
        this.status = status;
        this.backlogCount = backlogCount;
        this.published = published;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Double getRegisteredCredits() {
        return registeredCredits;
    }

    public void setRegisteredCredits(Double registeredCredits) {
        this.registeredCredits = registeredCredits;
    }

    public Double getEarnedCredits() {
        return earnedCredits;
    }

    public void setEarnedCredits(Double earnedCredits) {
        this.earnedCredits = earnedCredits;
    }

    public Double getSgpa() {
        return sgpa;
    }

    public void setSgpa(Double sgpa) {
        this.sgpa = sgpa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBacklogCount() {
        return backlogCount;
    }

    public void setBacklogCount(Integer backlogCount) {
        this.backlogCount = backlogCount;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
