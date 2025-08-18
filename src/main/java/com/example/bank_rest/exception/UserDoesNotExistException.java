package com.example.bank_rest.exception;

import com.example.bank_rest.repository.UserRepository;

public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException(String msg) {
        super(msg);
    }
}
