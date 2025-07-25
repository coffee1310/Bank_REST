package com.example.bank_rest.security;

import com.example.bank_rest.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;

public class InitialAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtService jwtService;

    public InitialAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            String contentType = request.getContentType();
            if (contentType == null || !contentType.equals("application/json")) {
                throw new RuntimeException("Content-Type must be 'application/json'");
            }

            byte[] requestBody = StreamUtils.copyToByteArray(request.getInputStream());
            if (requestBody.length == 0) {
                throw new RuntimeException("Request body is empty");
            }

            UserDTO userDTO = objectMapper.readValue(requestBody, UserDTO.class);
            if (userDTO.getUsername() == null || userDTO.getPassword() == null) {
                throw new RuntimeException("Username and password must be provided");
            }

            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(
                            userDTO.getUsername(),
                            userDTO.getPassword()
                    );

            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (IOException e) {
            throw new RuntimeException("Authentication request parsing failed", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        String jwt = jwtService.generateToken(authResult);
        response.addHeader("Authorization", "Bearer " + jwt);
        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + jwt + "\"}");
    }
}