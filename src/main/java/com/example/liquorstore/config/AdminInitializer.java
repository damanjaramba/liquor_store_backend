package com.example.liquorstore.config;

import com.example.liquorstore.entity.User;
import com.example.liquorstore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class AdminInitializer {
    @Bean
    public CommandLineRunner addAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Optional<User> admin = userRepository.findByUsername("admin");
            if (admin.isEmpty()) {
                User user = new User();
                user.setUsername("Dama");
                user.setPassword(passwordEncoder.encode("Dbit@nrb39"));
                user.setEmail("admin@example.com");
                user.setName("Default Admin");
                user.setRole("ADMIN");
                userRepository.save(user);
                System.out.println("Default admin created.");
            }
        };
    }
}
