package com.example.bank_rest.controller;

import com.example.bank_rest.dto.AuthResponse;
import com.example.bank_rest.dto.UserDTO;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.UsernameAlreadyExistsException;
import com.example.bank_rest.repository.UserRepository;
import com.example.bank_rest.service.AuthService;
import com.example.bank_rest.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registration(@RequestBody @Valid UserDTO userDTO)  {
        userDTO.validate();
        try {
            AuthResponse authResponse = authService.register(userDTO);
            return ResponseEntity.ok(authResponse);
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
