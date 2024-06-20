package com.rezero.rotto.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


// Firebase 설정을 관리하는 Config
@Configuration
@Slf4j
public class FirebaseConfig {

    private final ClassPathResource resource = new ClassPathResource("serviceAccountKey.json");


    // Firebase 를 초기화하는 메서드
    @Bean
    public FirebaseApp initializeFirebase() {
        try (InputStream serviceAccount = resource.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            return FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            log.error("Failed to initialize FirebaseApp", e);
            return null;
        }
    }
}
