package com.example.bank_rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
