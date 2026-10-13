package com.studentgrade.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MarksEntryDTO {

    @NotBlank(message = "Roll number is required")
    private String rollNumber;

    @NotBlank(message = "Subject code is required")
    private String subjectCode;

    @NotNull(message = "Semester is required")
    @Min(1)
    @Max(8)
    private Integer semester;

    @NotNull(message = "Internal marks are required")
    @DecimalMin(value = "0.0", message = "Internal marks cannot be negative")
    private Double internalMarks;

    @NotNull(message = "External marks are required")
    @DecimalMin(value = "0.0", message = "External marks cannot be negative")
    private Double externalMarks;

    public MarksEntryDTO() {
    }

    public MarksEntryDTO(String rollNumber, String subjectCode, Integer semester,
                         Double internalMarks, Double externalMarks) {
        this.rollNumber = rollNumber;
        this.subjectCode = subjectCode;
        this.semester = semester;
        this.internalMarks = internalMarks;
        this.externalMarks = externalMarks;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
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
}
