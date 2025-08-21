package com.ghartmann;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
    
}
