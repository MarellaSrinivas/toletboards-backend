package com.toletboards.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.toletboards.model.Role;
import com.toletboards.model.User;
import com.toletboards.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (!userRepository.existsByEmail("admin@toletboards.com")) {

            User admin = User.builder()
                    .fullName("System Admin")
                    .email("admin@toletboards.com")
                    .phone("9999999999")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ROLE_ADMIN)
                    .enabled(true)
                    .verified(true)
                    .build();

            userRepository.save(admin);

            System.out.println("===================================");
            System.out.println("Default Admin Created");
            System.out.println("Email : admin@toletboards.com");
            System.out.println("Password : Admin@123");
            System.out.println("===================================");
        }
    }
}