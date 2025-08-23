package com.ghartmann.service;

import com.ghartmann.dao.IUserDAO;
import com.ghartmann.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailVerificationService {

    @Autowired
    private IUserDAO userDAO;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRY_MINUTES = 15;
    private static final int MAX_ATTEMPTS = 3;
    
    // Gerar código numérico de 6 dígitos
    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10)); // Dígitos de 0-9
        }
        
        return code.toString();
    }
    
    // Calcular data de expiração (15 minutos)
    public LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusMinutes(CODE_EXPIRY_MINUTES);
    }
    
    // Verificar se o código expirou
    public boolean isCodeExpired(LocalDateTime expiryDate) {
        return expiryDate != null && expiryDate.isBefore(LocalDateTime.now());
    }
    
    // Enviar email com código
    public void sendVerificationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Seu código de verificação - NoRe");
        message.setText(
            "Olá " + user.getUsername() + ",\n\n" +
            "Seu código de verificação é: " + user.getVerificationCode() + "\n\n" +
            "Este código expirará em " + CODE_EXPIRY_MINUTES + " minutos.\n\n" +
            "Se você não solicitou este código, ignore este email.\n\n" +
            "Atenciosamente,\nEquipe NoRe"
        );
        
        mailSender.send(message);
    }
    
    // Verificar código
    public boolean verifyEmail(String code, String email) {
        User user = userDAO.getUserByVerificationCode(code);
        
        if (user == null || !user.getEmail().equals(email)) {
            return false; // Código não encontrado ou não corresponde ao email
        }
        
        // Verificar se o código expirou
        if (isCodeExpired(user.getCodeExpiryDate())) {
            return false; // Código expirado
        }
        
        // Verificar número máximo de tentativas
        if (user.getCodeAttempts() >= MAX_ATTEMPTS) {
            return false; // Muitas tentativas
        }
        
        return userDAO.verifyUserEmail(code);
    }
    
    // Reenviar código
    public boolean resendVerificationCode(String email) {
        User user = userDAO.getUserByEmail(email);
        
        if (user == null || user.isEmailVerified()) {
            return false;
        }
        
        // Gerar novo código
        user.setVerificationCode(generateVerificationCode());
        user.setCodeExpiryDate(calculateExpiryDate());
        user.setCodeAttempts(0);
        
        if (userDAO.updateUser(user)) {
            sendVerificationEmail(user);
            return true;
        }
        
        return false;
    }
    
    // Incrementar tentativas
    public void incrementVerificationAttempts(String email) {
        userDAO.incrementVerificationAttempts(email);
    }
}