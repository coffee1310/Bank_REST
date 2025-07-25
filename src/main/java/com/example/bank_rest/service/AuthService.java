package com.example.bank_rest.service;

import com.example.bank_rest.dto.AuthResponse;
import com.example.bank_rest.dto.UserDTO;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.UsernameAlreadyExistsException;
import com.example.bank_rest.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(UserDTO userDTO) throws UsernameAlreadyExistsException {
        User user = userService.registerNewUser(userDTO).get();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                )
        );

        String jwt = jwtService.generateToken(authentication);

        AuthResponse response = new AuthResponse();
        response.setToken(jwt);
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setExpiresIn(jwtService.getExpiration());

        return response;
    }
}