package com.example.bank_rest.exception;

import com.example.bank_rest.repository.UserRepository;

public class UserDoesNotExistException extends Exception {

    public UserDoesNotExistException(String msg) {
        super(msg);
    }
}
