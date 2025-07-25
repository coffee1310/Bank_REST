package com.example.bank_rest.service;

import com.example.bank_rest.dto.UserDTO;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.UsernameAlreadyExistsException;
import com.example.bank_rest.repository.UserRepository;
import com.example.bank_rest.util.converter.DTOConverterFactory;
import com.example.bank_rest.util.converter.EntityConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DTOConverterFactory converterFactory;

    @Transactional
    public Optional<User> registerNewUser(UserDTO userDTO) throws UsernameAlreadyExistsException {
        if (userRepository.existsUserByUsername(userDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        EntityConverter<User, UserDTO> converter = converterFactory
                .getConverter(User.class, UserDTO.class);

        User user = converter.toEntity(userDTO);
        return Optional.of(userRepository.save(user));
    }
}
