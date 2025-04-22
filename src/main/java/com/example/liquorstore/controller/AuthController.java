package com.example.liquorstore.controller;

import com.example.liquorstore.dto.Login;
import com.example.liquorstore.dto.SignUpDto;
import com.example.liquorstore.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.liquorstore.service.UserService;

import java.time.Instant;

@RestController
@RequestMapping("public/api/v1")
public class AuthController {
    private UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpDto user) {
        try {
            User signedUp = userService.signUp(user);
            if (signedUp != null) {
                return new ResponseEntity<User>(signedUp, HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @PostMapping("/login")
    public ResponseEntity<?> login( @RequestBody Login login) {
        try {
            ResponseEntity<?> responseWrapper = userService.login(login);
            return new ResponseEntity<>(responseWrapper.getBody(),responseWrapper.getStatusCode());
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>( "Invalid credentials provided. Please try again.", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // Log the unexpected error
            return new ResponseEntity<>("An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
