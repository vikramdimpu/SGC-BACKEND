package com.studentgrade.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subject_marks", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "subject_id", "semester"})
})
public class SubjectMarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false)
    private Double internalMarks = 0.0;

    @Column(nullable = false)
    private Double externalMarks = 0.0;

    @Column(nullable = false)
    private Double totalMarks = 0.0;

    @Column(nullable = false)
    private Double percentage = 0.0;

    @Column(nullable = false, length = 10)
    private String grade = "F";

    @Column(nullable = false)
    private Double gradePoints = 0.0;

    @Column(nullable = false, length = 10)
    private String status = "FAIL";

    @Column(nullable = false)
    private boolean published = false;

    public SubjectMarks() {
    }

    public SubjectMarks(Student student, Subject subject, Integer semester, Double internalMarks,
                        Double externalMarks, Double totalMarks, Double percentage, String grade,
                        Double gradePoints, String status, boolean published) {
        this.student = student;
        this.subject = subject;
        this.semester = semester;
        this.internalMarks = internalMarks;
        this.externalMarks = externalMarks;
        this.totalMarks = totalMarks;
        this.percentage = percentage;
        this.grade = grade;
        this.gradePoints = gradePoints;
        this.status = status;
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

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Double getInternalMarks() {
        return internalMarks;
    }

    public void setInternalMarks(Double internalMarks) {
        this.internalMarks = internalMarks;
    }

    public Double getExternalMarks() {
        return externalMarks;
    }

    public void setExternalMarks(Double externalMarks) {
        this.externalMarks = externalMarks;
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

    public Double getGradePoints() {
        return gradePoints;
    }

    public void setGradePoints(Double gradePoints) {
        this.gradePoints = gradePoints;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
