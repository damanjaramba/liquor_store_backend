package com.example.liquorstore.service;

import com.example.liquorstore.dto.Login;
import com.example.liquorstore.dto.LoginResponse;
import com.example.liquorstore.dto.SignUpDto;
import com.example.liquorstore.entity.User;
import com.example.liquorstore.helpers.JwtHelper;
import com.example.liquorstore.helpers.PhoneNumberHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.liquorstore.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signUp(SignUpDto signUpDto) {
        Optional<User> existingByUserName = userRepository.findByUsername(signUpDto.getUsername());
        Optional<User> existingByEmail = userRepository.findByEmail(signUpDto.getEmail());
        if (existingByUserName.isPresent() || existingByEmail.isPresent()) {
            throw new RuntimeException("User already exists by the userName or email");
        }
        PhoneNumberHelper phoneNumberHelper = new PhoneNumberHelper();
        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setMobileNumber(phoneNumberHelper.normalizeMobileNumber(signUpDto.getMobileNumber()));
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setName(signUpDto.getName());
        user.setRole("USER");

        return userRepository.save(user);
    }

    public ResponseEntity<?> login(Login login) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.username(), login.password())
            );

            User user = userRepository.findByUsername(login.username())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String token = JwtHelper.generateToken(user.getUsername());
            String refreshToken = JwtHelper.generateRefreshToken(user.getUsername());

            LoginResponse loginResponse = new LoginResponse(
                    user.getName(),
                    user.getEmail(),
                    user.getMobileNumber(),
                    token,
                    refreshToken,
                    user.getRole()
            );

            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials provided. Please try again.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(401).body("User not found. Please check your credentials.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred. Please try again later.");
        }
    }
}
