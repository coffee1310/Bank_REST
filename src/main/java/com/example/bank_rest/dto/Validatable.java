package com.example.bank_rest.dto;

import jakarta.validation.Validator;

public interface Validatable {
    void validate(Validator validator);
}
