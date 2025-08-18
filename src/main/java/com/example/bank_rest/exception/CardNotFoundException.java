package com.example.bank_rest.exception;

public class CardNotFoundException extends Exception {

    public CardNotFoundException(String msg) {
        super(msg);
    }
}
