package com.ghartmann.validations;
import com.ghartmann.dto.UserRegisterDTO;

public class UserValidations {

    public void validateUser(UserRegisterDTO user) {
        validateName(user.getNome());
        validateUserName(user.getNome());
        validateEmail(user.getEmail());
        validatePassword(user.getSenha());
    }

    public void validateName(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        if (nome.length() < 3 || nome.length() > 100) {
            throw new IllegalArgumentException("Nome deve ter entre 3 e 100 caracteres");
        }
        if (!nome.matches("^[A-Za-zÀ-ú ]+$")) {
            throw new IllegalArgumentException("Nome deve conter apenas letras e espaços");
        }
    }

    public void validateUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Nome de Usuário não pode ser nulo ou vazio");
        }
        if (userName.length() < 3 || userName.length() > 30) {
            throw new IllegalArgumentException("Nome de Usuário deve ter entre 3 e 30 caracteres");
        }
    }

    public void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }
        if (password.length() < 6 || password.length() > 12) {
            throw new IllegalArgumentException("Senha deve ter entre 8 e 12 caracteres");
        }
        if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*[!@#$%^&*()].*")) {
            throw new IllegalArgumentException("Senha deve conter ao menos uma letra maiúscula, uma minúscula e um caractere especial");
        }
    }
    
}
