package com.studentgrade.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubjectDTO {

    private Long id;

    @NotBlank(message = "Subject code is required")
    private String code;

    @NotBlank(message = "Subject name is required")
    private String name;

    @NotBlank(message = "Branch is required")
    private String branch = "CSE";

    @NotNull(message = "Semester is required")
    @Min(1)
    @Max(8)
    private Integer semester;

    @NotBlank(message = "Subject type is required (THEORY or LABORATORY)")
    private String type = "THEORY";

    @NotNull(message = "Credits are required")
    @DecimalMin("0.5")
    private Double credits = 3.0;

    @NotNull(message = "Maximum internal marks are required")
    @DecimalMin("0.0")
    private Double internalMaxMarks = 30.0;

    @NotNull(message = "Maximum external marks are required")
    @DecimalMin("0.0")
    private Double externalMaxMarks = 70.0;

    @NotNull(message = "Minimum internal pass marks are required")
    @DecimalMin("0.0")
    private Double internalMinPassMarks = 0.0;

    @NotNull(message = "Minimum external pass marks are required")
    @DecimalMin("0.0")
    private Double externalMinPassMarks = 25.0;

    @NotNull(message = "Total minimum pass marks are required")
    @DecimalMin("0.0")
    private Double totalMinPassMarks = 40.0;

    public SubjectDTO() {
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
