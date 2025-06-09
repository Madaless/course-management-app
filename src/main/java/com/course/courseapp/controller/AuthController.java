package com.course.courseapp.controller;

import com.course.courseapp.dto.AuthRequest;
import com.course.courseapp.dto.AuthResponse;
import com.course.courseapp.entity.AppUser;
import com.course.courseapp.repository.AppUserRepository;
import com.course.courseapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
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
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        AppUser user = appUserRepository.findByEmail(request.email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(request.password, user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

}
