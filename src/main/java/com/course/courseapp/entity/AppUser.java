package com.course.courseapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public AppUser(String email, String password, Role role) {
        this.email= email;
        this.password = password;
        this.role =role;
    }
}