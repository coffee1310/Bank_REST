package com.example.bank_rest.util.Encryptor;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeyTemplates;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DeterministicEncryptor {
    private final Aead aead;

    public DeterministicEncryptor() throws Exception {
        AeadConfig.register();
        KeysetHandle handle = KeysetHandle.generateNew(KeyTemplates.get("AES256_SIV"));
        this.aead = handle.getPrimitive(Aead.class);
    }

    public DeterministicEncryptor(String keyBase64) throws Exception {
        AeadConfig.register();
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        KeysetHandle handle = KeysetHandle.generateNew(KeyTemplates.get("AES256_SIV"));
        this.aead = handle.getPrimitive(Aead.class);
    }

    public String encryptDeterministically(String cardNumber) throws Exception {
        byte[] encrypted = aead.encrypt(
                cardNumber.getBytes(StandardCharsets.UTF_8),
                new byte[0]
        );
        return Base64.getEncoder().encodeToString(encrypted);
    }
}