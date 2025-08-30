package com.example.bank_rest.entity;

import com.example.bank_rest.exception.InsufficientFundsException;

import java.math.BigDecimal;

public interface IBalanceOperations {
    void deposit(BigDecimal amount);
    void withdraw(BigDecimal amount) throws InsufficientFundsException;
}
