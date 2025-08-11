package com.example.bank_rest.dto;

import com.example.bank_rest.util.annotation.UserExists;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class CardDTO implements Validatable {

    private Long id;

    @NotBlank
    @Pattern(regexp = "^\\d{4} \\d{4} \\d{4} \\d{4}$")
    private String card_number;

    @Future
    private Date expiry_date;

    @UserExists
    private Long user_id;

    @NotNull
    private BigDecimal balance;

    @NotBlank
    private String status;

    @NotNull
    private String maskedNumber;

    @Override
    public void validate(Validator validator) {
        Set<ConstraintViolation<CardDTO>> violations = validator.validate(this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
