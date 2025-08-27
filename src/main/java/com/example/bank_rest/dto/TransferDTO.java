package com.example.bank_rest.dto;

import com.example.bank_rest.util.annotation.CardExists;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
public class TransferDTO implements Validatable {

    private Long id;

    @CardExists
    private Long card_from_id;

    @CardExists
    private Long card_to_id;

    @Min(1)
    private BigDecimal amount;

    @PastOrPresent
    private LocalDateTime created_at;


    @Override
    public void validate(Validator validator) {
        Set<ConstraintViolation<TransferDTO>> violations = validator.validate(this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
