package com.ghartmann.validations;

public class UserValidations {

    public void validateUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (userName.length() < 3 || userName.length() > 50) {
            throw new IllegalArgumentException("Username must be between 3 and 50 characters long");
        }
    }

    public void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (password.length() < 6 || password.length() > 100) {
            throw new IllegalArgumentException("Password must be between 6 and 100 characters long");
        }
    }
    
}
