package com.example.liquorstore.service;

import com.example.liquorstore.entity.User;
import com.example.liquorstore.helpers.JwtHelper;
import com.example.liquorstore.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.Optional;

@Service
public class SessionUserService {
    private final UserRepository userRepository;
    public SessionUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getSessionUser(){
        HttpServletRequest req = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String header = req.getHeader("Authorization");
        String token = header.replace("Bearer ", "");

        String identifier = JwtHelper.extractUsername(token);

        Optional<User> user = userRepository.findByUsername(identifier);
        return user.orElseThrow(
                () -> new RuntimeException("User not found")
        );

    }
}
