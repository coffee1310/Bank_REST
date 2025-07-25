package com.example.bank_rest.util.converter;

import com.example.bank_rest.dto.UserDTO;
import com.example.bank_rest.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements EntityConverter<User, UserDTO> {

    @Override
    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());

        return user;
    }
}
