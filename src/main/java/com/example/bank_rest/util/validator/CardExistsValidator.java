package com.example.bank_rest.util.validator;

import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.util.annotation.CardExists;
import com.example.bank_rest.util.annotation.UserExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CardExistsValidator implements ConstraintValidator<CardExists, Long> {

    private final CardRepository cardRepository;

    @Override
    public boolean isValid(Long cardId, ConstraintValidatorContext constraintValidatorContext) {
        if (cardId == null) {
            return false;
        }

        return cardRepository.existsCardById(cardId);
    }
}
