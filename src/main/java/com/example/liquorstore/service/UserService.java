package com.example.liquorstore.service;

import com.example.liquorstore.dto.SignUpDto;
import com.example.liquorstore.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.liquorstore.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(SignUpDto signUpDto) {
        Optional<User> existingByUserName = userRepository.findByUsername(signUpDto.getUsername());
        Optional<User> existingByEmail = userRepository.findByEmail(signUpDto.getEmail());
        if (existingByUserName.isPresent() || existingByEmail.isPresent()) {
            throw new RuntimeException("User already exists by the userName or email");
        }
        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(signUpDto.getPassword());
        user.setName(signUpDto.getName());
        user.setRole("USER");

        return userRepository.save(user);
    }
}
