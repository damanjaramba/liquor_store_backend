package com.example.liquorstore.controller;

import com.example.liquorstore.dto.SignUpDto;
import com.example.liquorstore.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.liquorstore.service.UserService;

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
}
