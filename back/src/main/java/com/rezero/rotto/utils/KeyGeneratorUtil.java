package com.rezero.rotto.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class KeyGeneratorUtil {

    public static void generateAndSaveKey(String filepath) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // AES 256-bit 키
        SecretKey secretKey = keyGenerator.generateKey();

        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        Files.write(Paths.get(filepath), encodedKey.getBytes());
        System.out.println("Key has been generated and saved to: " + filepath);
}

    public static void main(String[] args) throws Exception {
        // 현재 프로젝트 경로를 기준으로 상대 경로 설정
        String projectPath = System.getProperty("user.dir");
        String relativePath = "/src/main/resources/aes_yml.txt";
        generateAndSaveKey(projectPath + relativePath);
    }

}
