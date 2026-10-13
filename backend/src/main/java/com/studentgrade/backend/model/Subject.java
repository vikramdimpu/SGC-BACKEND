package com.studentgrade.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 50)
    private String branch = "CSE";

    @Column(nullable = false)
    private Integer semester = 1;

    @Column(nullable = false, length = 20)
    private String type = "THEORY"; // THEORY or LABORATORY

    @Column(nullable = false)
    private Double credits = 3.0;

    @Column(nullable = false)
    private Double internalMaxMarks = 30.0;

    @Column(nullable = false)
    private Double externalMaxMarks = 70.0;

    @Column(nullable = false)
    private Double internalMinPassMarks = 0.0;

    @Column(nullable = false)
    private Double externalMinPassMarks = 25.0;

    @Column(nullable = false)
    private Double totalMinPassMarks = 40.0;

    public Subject() {
    }

    public Subject(String code, String name, String branch, Integer semester, String type,
                   Double credits, Double internalMaxMarks, Double externalMaxMarks,
                   Double internalMinPassMarks, Double externalMinPassMarks, Double totalMinPassMarks) {
        this.code = code;
        this.name = name;
        this.branch = branch;
        this.semester = semester;
        this.type = type;
        this.credits = credits;
        this.internalMaxMarks = internalMaxMarks;
        this.externalMaxMarks = externalMaxMarks;
        this.internalMinPassMarks = internalMinPassMarks;
        this.externalMinPassMarks = externalMinPassMarks;
        this.totalMinPassMarks = totalMinPassMarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public Double getInternalMaxMarks() {
        return internalMaxMarks;
    }

    public void setInternalMaxMarks(Double internalMaxMarks) {
        this.internalMaxMarks = internalMaxMarks;
    }

    public Double getExternalMaxMarks() {
        return externalMaxMarks;
    }

    public void setExternalMaxMarks(Double externalMaxMarks) {
        this.externalMaxMarks = externalMaxMarks;
    }

    public Double getInternalMinPassMarks() {
        return internalMinPassMarks;
    }

    public void setInternalMinPassMarks(Double internalMinPassMarks) {
        this.internalMinPassMarks = internalMinPassMarks;
    }

    public Double getExternalMinPassMarks() {
        return externalMinPassMarks;
    }

    public void setExternalMinPassMarks(Double externalMinPassMarks) {
        this.externalMinPassMarks = externalMinPassMarks;
    }

    public Double getTotalMinPassMarks() {
        return totalMinPassMarks;
    }

    public void setTotalMinPassMarks(Double totalMinPassMarks) {
        this.totalMinPassMarks = totalMinPassMarks;
    }
}
