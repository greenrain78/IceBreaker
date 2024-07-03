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
}