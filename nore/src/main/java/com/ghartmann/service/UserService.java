package com.ghartmann.service;

import com.ghartmann.dao.IUserDAO;
import com.ghartmann.domain.User;
import com.ghartmann.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final IUserDAO userDAO;
    private final EmailVerificationService emailVerificationService;
    private final PasswordUtil passwordUtil;

    @Autowired
    public UserService(IUserDAO userDAO, EmailVerificationService emailVerificationService) {
        this.userDAO = userDAO;
        this.emailVerificationService = emailVerificationService;
        this.passwordUtil = new PasswordUtil();
    }

    public boolean emailExists(String email) {
        return userDAO.getUserByEmail(email) != null;
    }

    public User registerUser(User user) {
        // Verificar se email já existe
        if (emailExists(user.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        // Hash da senha
         String hash = passwordUtil.hashPassword(user.getPassword());
        user.setPasswordHash(hash);

        // Configurar verificação por código
        user.setEmailVerified(false);
        user.setVerificationCode(emailVerificationService.generateVerificationCode());
        user.setCodeExpiryDate(emailVerificationService.calculateExpiryDate());
        user.setCodeAttempts(0);

        if (userDAO.registerUser(user)) {
            // Enviar email com código
            emailVerificationService.sendVerificationEmail(user);
            return user;
        }

        throw new RuntimeException("Erro ao registrar usuário");
    }
    
    public boolean verifyEmail(String code, String email) {
        return emailVerificationService.verifyEmail(code, email);
    }

    public void incrementVerificationAttempts(String email) {
        userDAO.incrementVerificationAttempts(email);
    }
    
    public boolean resendVerificationCode(String email) {
        return emailVerificationService.resendVerificationCode(email);
    }

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public boolean isEmailVerified(String email) {
        User user = userDAO.getUserByEmail(email);
        return user != null && user.isEmailVerified();
    }

    public boolean loginUser(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email não verificado. Verifique sua caixa de entrada.");
        }

        return userDAO.loginUser(email, password);
    }
}