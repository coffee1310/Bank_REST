package com.example.bank_rest.util.validator;

import com.example.bank_rest.repository.UserRepository;
import com.example.bank_rest.util.annotation.UserExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserExistsValidator implements ConstraintValidator<UserExists, Long> {

    private final UserRepository userRepository;

    public UserExistsValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext context) {
        if (userId == null) {
            return false;
        }

        boolean userExists = userRepository.existsById(userId);

        return userExists;
    }
}
