package com.studentgrade.backend.dto;

import jakarta.validation.constraints.*;

public class StudentRequestDTO {

    @NotBlank(message = "Student name is required")
    @Size(min = 2, max = 100, message = "Student name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Roll number is required")
    @Size(min = 1, max = 30, message = "Roll number cannot exceed 30 characters")
    private String rollNumber;

    @NotNull(message = "Subject 1 mark is required")
    @DecimalMin(value = "0.0", message = "Subject 1 marks must be at least 0")
    @DecimalMax(value = "100.0", message = "Subject 1 marks cannot exceed 100")
    private Double subject1;

    @NotNull(message = "Subject 2 mark is required")
    @DecimalMin(value = "0.0", message = "Subject 2 marks must be at least 0")
    @DecimalMax(value = "100.0", message = "Subject 2 marks cannot exceed 100")
    private Double subject2;

    @NotNull(message = "Subject 3 mark is required")
    @DecimalMin(value = "0.0", message = "Subject 3 marks must be at least 0")
    @DecimalMax(value = "100.0", message = "Subject 3 marks cannot exceed 100")
    private Double subject3;

    @NotNull(message = "Subject 4 mark is required")
    @DecimalMin(value = "0.0", message = "Subject 4 marks must be at least 0")
    @DecimalMax(value = "100.0", message = "Subject 4 marks cannot exceed 100")
    private Double subject4;

    @NotNull(message = "Subject 5 mark is required")
    @DecimalMin(value = "0.0", message = "Subject 5 marks must be at least 0")
    @DecimalMax(value = "100.0", message = "Subject 5 marks cannot exceed 100")
    private Double subject5;

    public StudentRequestDTO() {
    }

    public StudentRequestDTO(String name, String rollNumber, Double subject1, Double subject2,
                             Double subject3, Double subject4, Double subject5) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.subject1 = subject1;
        this.subject2 = subject2;
        this.subject3 = subject3;
        this.subject4 = subject4;
        this.subject5 = subject5;
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
}
