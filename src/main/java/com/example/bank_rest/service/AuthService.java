package com.example.bank_rest.service;

import com.example.bank_rest.dto.AuthRequest;
import com.example.bank_rest.dto.AuthResponse;
import com.example.bank_rest.dto.UserDTO;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.UsernameAlreadyExistsException;
import com.example.bank_rest.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(UserDTO userDTO) throws UsernameAlreadyExistsException {
        User user = userService.registerNewUser(userDTO).get();

        String jwt = jwtService.generateTokenForUser(user);

        return AuthResponse.builder()
                .token(jwt)
                .username(user.getUsername())
                .role(user.getRole())
                .expiresIn(jwtService.getExpiration())
                .build();
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        passwordEncoder.encode(authRequest.getPassword())
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("USER");

        return AuthResponse.builder()
                .token(jwt)
                .username(userDetails.getUsername())
                .role(role)
                .expiresIn(jwtService.getExpiration())
                .build();
    }
}