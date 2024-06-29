package com.example.IceBreaking.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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