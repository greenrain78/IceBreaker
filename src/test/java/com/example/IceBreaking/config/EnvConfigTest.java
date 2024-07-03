package com.example.IceBreaking.config;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EnvConfigTest {
    @Test
    public void test() {
        try {
                // gcloud auth print-access-token 명령 실행
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("cmd.exe", "/c", "gcloud auth print-access-token");
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
                String accessToken = output.toString();
                System.out.println("Access Token: " + accessToken);
            } else {
                // 에러가 발생한 경우
                System.err.println("Error: Failed to get access token");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
