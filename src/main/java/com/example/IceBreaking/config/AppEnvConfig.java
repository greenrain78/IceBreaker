package com.example.IceBreaking.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppEnvConfig {
    @Value("${API_KEY:None}")
    private String apiKey;
}