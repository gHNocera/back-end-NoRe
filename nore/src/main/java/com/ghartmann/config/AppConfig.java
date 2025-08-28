package com.ghartmann.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ghartmann.PasswordUtil;

@Configuration
public class AppConfig {


    @Bean
    public PasswordUtil passwordUtil() {
        return new PasswordUtil();
    }
}