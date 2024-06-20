package com.rezero.rotto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class CryptoConfig {

    @Value("${AES_KEY}")
    private String base64EncodedKey;

    @Bean
    public SecretKeySpec aesKey() {
        // Base64 디코딩을 java.util.Base64로 처리
        byte[] decodedKey = Base64.getDecoder().decode(base64EncodedKey);
        return new SecretKeySpec(decodedKey, "AES");
    }
}
