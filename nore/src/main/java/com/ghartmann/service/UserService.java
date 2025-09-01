package com.ghartmann.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ghartmann.domain.User;
import com.ghartmann.dto.UserRegisterDTO;
import com.ghartmann.dto.UserResponseDTO;
import com.ghartmann.mapper.UserMapper;
import com.ghartmann.repository.UserRepository;
import com.ghartmann.validations.UserValidations;
import com.ghartmann.PasswordUtil;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;
    private final PasswordUtil passwordUtil;
    private final UserValidations userValidations = new UserValidations();

    @Autowired
    public UserService(UserRepository userRepository, EmailVerificationService emailVerificationService) {
        this.userRepository = userRepository;
        this.emailVerificationService = emailVerificationService;
        this.passwordUtil = new PasswordUtil();
    }

    /** Verifica se email já está cadastrado */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /** Registro de usuário */
    @Transactional
    public UserResponseDTO registerUser(UserRegisterDTO dto) {
        if(emailExists(dto.getEmail())) {
            throw new RuntimeException("Email já esta em uso");
        }
        userValidations.validateUser(dto);
        String hash = passwordUtil.hashPassword(dto.getSenha());
        User user = UserMapper.toEntity(dto, hash);

        // Geração de código de verificação
        user.setCodigo(emailVerificationService.generateVerificationCode());
        user.setCodeExpiryDate(emailVerificationService.calculateExpiryDate());

        User savedUser = userRepository.save(user);

        // Envio do email de verificação
        emailVerificationService.sendVerificationEmail(savedUser);

        return UserMapper.toDTO(savedUser);
    }

    /** Login de usuário */
    public boolean loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if(!user.isEmailVerified()) {
            throw new RuntimeException("Email não verificado. Verifique sua caixa de entrada.");
        }

        return passwordUtil.verifyPassword(password, user.getPasswordHash());
    }

    /** Verificação de email */
    @Transactional
    public boolean verifyEmail(String code) {
        Optional<User> optionalUser = userRepository.findByCodigo(code);
        if(optionalUser.isEmpty()) return false;

        User user = optionalUser.get();
        user.setEmailVerified(true);
        user.setCodigo(null);
        user.setCodeAttempts(0);

        userRepository.save(user);
        return true;
    }

    /** Incremento de tentativas de verificação */
    @Transactional
    public void incrementVerificationAttempts(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.incrementCodeAttempts();
        userRepository.save(user);
    }

    /** Reenvio de código de verificação */
    @Transactional
    public boolean resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String code = emailVerificationService.generateVerificationCode();
        user.setCodigo(code);
        user.setCodeExpiryDate(emailVerificationService.calculateExpiryDate());
        userRepository.save(user);

        emailVerificationService.sendVerificationEmail(user);
        return true;
    }

    /** Verifica se o email já foi confirmado */
    public boolean isEmailVerified(String email) {
        return userRepository.findByEmail(email)
                .map(User::isEmailVerified)
                .orElse(false);
    }

    /** Busca usuário pelo email */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /** Atualiza um usuário existente a partir do DTO */
    @Transactional
    public UserResponseDTO updateUser(UserRegisterDTO dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String hash = (dto.getSenha() != null && !dto.getSenha().isEmpty())
                ? passwordUtil.hashPassword(dto.getSenha())
                : null;

        UserMapper.updateEntity(user, dto, hash);

        User updated = userRepository.save(user);
        return UserMapper.toDTO(updated);
    }
}
