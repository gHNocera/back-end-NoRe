package com.ghartmann.service;

import com.ghartmann.domain.User;
import com.ghartmann.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailVerificationService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRY_MINUTES = 15;
    private static final int MAX_ATTEMPTS = 3;

    @Autowired
    public EmailVerificationService(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    /** Gerar código numérico de 6 dígitos */
    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    /** Calcular data de expiração */
    public LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusMinutes(CODE_EXPIRY_MINUTES);
    }

    /** Verificar se o código expirou */
    public boolean isCodeExpired(LocalDateTime expiryDate) {
        return expiryDate != null && expiryDate.isBefore(LocalDateTime.now());
    }

    /** Enviar email com código */
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

    /** Verificar código de email */
    @Transactional
    public boolean verifyEmail(String code, String email) {
        Optional<User> optionalUser = userRepository.findByVerificationCode(code);

        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();

        if (!user.getEmail().equals(email)) return false;
        if (isCodeExpired(user.getCodeExpiryDate())) return false;
        if (user.getCodeAttempts() >= MAX_ATTEMPTS) return false;

        user.setEmailVerified(true);
        user.setVerificationCode(null);
        user.setCodeAttempts(0);
        user.setCodeExpiryDate(null);

        userRepository.save(user);
        return true;
    }

    /** Reenviar código de verificação */
    @Transactional
    public boolean resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();

        if (user.isEmailVerified()) return false;

        user.setVerificationCode(generateVerificationCode());
        user.setCodeExpiryDate(calculateExpiryDate());
        user.setCodeAttempts(0);

        userRepository.save(user);
        sendVerificationEmail(user);
        return true;
    }

    /** Incrementar tentativas de verificação */
    @Transactional
    public void incrementVerificationAttempts(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.incrementCodeAttempts();
            userRepository.save(user);
        });
    }
}
