package com.course.courseapp.controller;

import com.course.courseapp.dto.AuthRequest;
import com.course.courseapp.dto.AuthResponse;
import com.course.courseapp.entity.AppUser;
import com.course.courseapp.exception.UserNotFoundException;
import com.course.courseapp.repository.AppUserRepository;
import com.course.courseapp.util.CourseApiResponse;
import com.course.courseapp.util.CourseResponseMessages;
import com.course.courseapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Value("${app.user.email}")
    private String configuredEmail;

    @Value("${app.user.password}")
    private String configuredPassword;

    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtUtil jwtUtil, AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<CourseApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        AppUser user = appUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.email()));

        if (passwordEncoder.matches(request.password(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
            AuthResponse authResponse = new AuthResponse(token);
            return ResponseEntity.ok(CourseApiResponse.success(CourseResponseMessages.UPDATED, authResponse, HttpStatus.OK.value()));
        } else {
            return ResponseEntity.ok(CourseApiResponse.fail(CourseResponseMessages.LOGGED_IN, HttpStatus.UNAUTHORIZED.value()));
        }
    }

}
