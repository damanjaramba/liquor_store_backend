package com.example.liquorstore.dto;

public record LoginResponse(
        String name,
        String email,
        String mobileNumber,
        String token,
        String refreshToken
) {
}
