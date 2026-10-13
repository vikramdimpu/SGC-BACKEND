package com.studentgrade.backend.dto;

import java.util.ArrayList;
import java.util.List;

public class SemesterAcademicSummaryDTO {

    private Integer semester;
    private Double registeredCredits = 0.0;
    private Double earnedCredits = 0.0;
    private Double sgpa = 0.0;
    private String status = "PENDING";
    private Integer backlogCount = 0;
    private boolean published = false;
    private List<SubjectMarkDetailDTO> subjects = new ArrayList<>();

    public SemesterAcademicSummaryDTO() {
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

    public List<SubjectMarkDetailDTO> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectMarkDetailDTO> subjects) {
        this.subjects = subjects;
    }
}
