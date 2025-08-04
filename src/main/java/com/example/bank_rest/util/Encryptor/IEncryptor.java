package com.example.bank_rest.util.Encryptor;

public interface IEncryptor {
    String encrypt(String unencrypted) throws Exception;
    String decrypt(String encrypted) throws Exception;
}
