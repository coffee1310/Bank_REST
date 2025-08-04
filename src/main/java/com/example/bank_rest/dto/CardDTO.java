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
    @Size(min = 19, max = 19)
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
    private String MaskedNumber;

    @Override
    public void validate() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<CardDTO>> violations = validator.validate(this);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
