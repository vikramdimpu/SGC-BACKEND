package com.studentgrade.backend.dto;

import com.studentgrade.backend.model.Role;

public class LoginResponse {

    private String username;
    private String role;
    private String name;
    private String rollNumber;
    private boolean active;
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(String username, String role, String name, String rollNumber, boolean active, String message) {
        this.username = username;
        this.role = role;
        this.name = name;
        this.rollNumber = rollNumber;
        this.active = active;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role != null ? role.name() : null;
    }

    public void setRole(String role) {
        this.role = role;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
