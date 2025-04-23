package com.example.liquorstore.service;

import com.example.liquorstore.entity.User;
import com.example.liquorstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) {

        User user = userRepository.findByUsername(userName).orElseThrow(() ->
                new RuntimeException(String.format("User does not exist with userName %s", userName)));
        if (user.getPassword() == null)
            throw new RuntimeException("The pin is empty");
        return org.springframework.security.core.userdetails.User.builder()
                .username(userName)
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }




}
