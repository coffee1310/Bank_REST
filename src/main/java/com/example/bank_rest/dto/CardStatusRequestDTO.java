package com.example.bank_rest.dto;

import com.example.bank_rest.util.annotation.CardExists;
import jakarta.persistence.Version;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class CardStatusRequestDTO implements Validatable {

    private Long id;

    @CardExists
    @NotNull
    @NotBlank
    private Long card_id;

    @Pattern(regexp = "ACTIVE|EXPIRED|BLOCKED", message = "Status must be either ACTIVE or BLOCKED")
    @NotNull
    @NotBlank
    private String status;

    @Override
    public void validate(Validator validator) {
        Set<ConstraintViolation<CardStatusRequestDTO>> violations = validator.validate(this);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
