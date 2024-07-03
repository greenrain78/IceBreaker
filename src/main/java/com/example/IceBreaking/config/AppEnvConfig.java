package com.example.IceBreaking.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Getter
@Component
public class AppEnvConfig {
    @Value("${API_KEY:None}")
    private String apiKey;

    @Value("${DEEPL_API_KEY:None}")
    private String deeplApiSecret;

    @Value("${SONNET_API_KEY:None}")
    private String sonnetApiKey;

    @Value("${ADMIN_EMAIL:None}")
    private String adminEmail;

    public void refreshSonnetAPI() {
        log.info("Refreshing Sonnet API Key...");
        try {
            // gcloud auth print-access-token 명령 실행
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("gcloud", "auth", "print-access-token", adminEmail);
            Process process = processBuilder.start();

            // 명령 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            // 프로세스 종료 코드 확인
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                // 성공적으로 액세스 토큰을 가져온 경우
                sonnetApiKey = output.toString();
                log.info("Sonnet API Key: " + sonnetApiKey);
            } else {
                // 에러가 발생한 경우
                log.error("Error: Failed to get access token - default: " + sonnetApiKey);
            }
        } catch (IOException | InterruptedException e) {
            log.error("Failed to get access token - default: " + sonnetApiKey, e);
        }
    }
    @PostConstruct
    public void init() {
        refreshSonnetAPI();
    }
}