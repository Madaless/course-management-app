package com.course.courseapp.config;

import com.course.courseapp.entity.AppUser;
import com.course.courseapp.entity.Role;
import com.course.courseapp.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataLoader {

    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public DataLoader(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        if (repo.count() == 0) {
            repo.save(new AppUser("admin@example.com", encoder.encode("admin123"), Role.ADMIN));
            repo.save(new AppUser("user@example.com", encoder.encode("user123"), Role.USER));
        }
    }
}