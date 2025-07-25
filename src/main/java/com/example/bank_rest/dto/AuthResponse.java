package com.example.bank_rest.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String username;
    private String role;

    private Long expiresIn;
}
