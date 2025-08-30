package com.example.bank_rest.exception;

public class CardDoesNotFound extends RuntimeException {

    public CardDoesNotFound(String msg) {
        super(msg);
    }
}
