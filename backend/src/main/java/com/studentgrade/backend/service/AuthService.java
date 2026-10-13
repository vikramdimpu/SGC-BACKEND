package com.studentgrade.backend.service;

import com.studentgrade.backend.dto.ChangePasswordRequest;
import com.studentgrade.backend.dto.LoginRequest;
import com.studentgrade.backend.dto.LoginResponse;
import com.studentgrade.backend.exception.ResourceNotFoundException;
import com.studentgrade.backend.model.Role;
import com.studentgrade.backend.model.Student;
import com.studentgrade.backend.model.User;
import com.studentgrade.backend.repository.StudentRepository;
import com.studentgrade.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public AuthService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String username = request.getUsername().trim();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials or inactive account."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials or inactive account.");
        }

        if (!user.isActive()) {
            throw new BadCredentialsException("Account is currently deactivated. Please contact your college administrator.");
        }

        // Establish Spring Security context
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Bind security context to HTTP session using Spring Security 6+ SecurityContextRepository
        securityContextRepository.saveContext(securityContext, httpRequest, httpResponse);

        String displayName = "Administrator";
        String rollNumber = null;

        if (user.getRole() == Role.ROLE_STUDENT) {
            Student student = studentRepository.findByRollNumber(user.getUsername()).orElse(null);
            if (student != null) {
                displayName = student.getName();
                rollNumber = student.getRollNumber();
            } else {
                displayName = user.getUsername();
                rollNumber = user.getUsername();
            }
        }

        return new LoginResponse(
                user.getUsername(),
                user.getRole().name(),
                displayName,
                rollNumber,
                user.isActive(),
                "Login successful."
        );
    }

    @Transactional(readOnly = true)
    public LoginResponse getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        String displayName = "Administrator";
        String rollNumber = null;

        if (user.getRole() == Role.ROLE_STUDENT) {
            Student student = studentRepository.findByRollNumber(user.getUsername()).orElse(null);
            if (student != null) {
                displayName = student.getName();
                rollNumber = student.getRollNumber();
            }
        }

        return new LoginResponse(
                user.getUsername(),
                user.getRole().name(),
                displayName,
                rollNumber,
                user.isActive(),
                "Authenticated user details."
        );
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
