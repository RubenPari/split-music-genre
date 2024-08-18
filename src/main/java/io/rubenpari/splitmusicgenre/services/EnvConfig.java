package io.rubenpari.splitmusicgenre.services;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    private final Dotenv dotenv;

    public EnvConfig() {
        this.dotenv = loadEnv();
    }

    private Dotenv loadEnv() {
        try {
            return Dotenv.load();
        } catch (Exception e) {
            throw new RuntimeException("Error loading .env file: " + e.getMessage(), e);
        }
    }

    public String getValue(String key) {
        return dotenv.get(key);
    }
}