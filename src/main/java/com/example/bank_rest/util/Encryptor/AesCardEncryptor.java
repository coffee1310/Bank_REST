package com.example.bank_rest.util.Encryptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AesCardEncryptor implements IEncryptor{

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    private final SecretKey secretKey;

    @Autowired
    public AesCardEncryptor(@Value("${encryption.aes.key}") String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Encryption key cannot be empty");
        }
        this.secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
    }
    @Override
    public String encrypt(String cardNumber) throws Exception {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

        byte[] encrypted = cipher.doFinal(cardNumber.getBytes(StandardCharsets.UTF_8));
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    @Override
    public String decrypt(String encryptedCard) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedCard);
        byte[] iv = new byte[IV_LENGTH];
        System.arraycopy(combined, 0, iv, 0, iv.length);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

        byte[] encrypted = new byte[combined.length - IV_LENGTH];
        System.arraycopy(combined, IV_LENGTH, encrypted, 0, encrypted.length);

        return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
    }
}
