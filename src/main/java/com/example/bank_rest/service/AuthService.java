package com.example.bank_rest.service;

import com.example.bank_rest.dto.AuthRequest;
import com.example.bank_rest.dto.AuthResponse;
import com.example.bank_rest.dto.UserDTO;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.UserDoesNotExistException;
import com.example.bank_rest.exception.UsernameAlreadyExistsException;
import com.example.bank_rest.repository.UserRepository;
import com.example.bank_rest.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserRepository userRepository;

    public AuthResponse register(UserDTO userDTO) throws UsernameAlreadyExistsException, UserDoesNotExistException {
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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword() // Не кодируем - AuthenticationManager сделает это
                )
        );

        // Получаем и UserDetails, и вашу сущность User
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Генерируем токен с учетом и UserDetails, и вашей сущности
        String jwt = jwtService.generateToken(userDetails, user);

        String role = user.getRole(); // Берем роль из вашей сущности

        return AuthResponse.builder()
                .token(jwt)
                .username(user.getUsername())
                .role(role)
                .expiresIn(jwtService.getExpiration())
                .build();
    }
}