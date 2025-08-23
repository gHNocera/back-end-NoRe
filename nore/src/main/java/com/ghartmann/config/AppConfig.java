package com.ghartmann.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ghartmann.PasswordUtil;
import com.ghartmann.dao.IUserDAO;
import com.ghartmann.dao.UserDAO;

@Configuration
public class AppConfig {

    @Bean
    public IUserDAO userDAO() {
        return new UserDAO();
    }

    @Bean
    public PasswordUtil passwordUtil() {
        return new PasswordUtil();
    }
}