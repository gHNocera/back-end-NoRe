package com.ghartmann.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ghartmann.domain.User;
import com.ghartmann.dto.EmailDTO;
import com.ghartmann.dto.UserRegisterDTO;
import com.ghartmann.dto.UserResponseDTO;
import com.ghartmann.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO userDTO) {
        try {
            UserResponseDTO registeredUser = userService.registerUser(userDTO);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuário cadastrado. Verifique seu email para o código de confirmação.");
            response.put("status", "success");
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailDTO request) {
        try {
            boolean verified = userService.verifyEmail(request.getCode());
            
            if (verified) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Email verificado com sucesso!");
                response.put("status", "success");
                return ResponseEntity.ok(response);
            } else {
                userService.incrementVerificationAttempts(request.getEmail());
                
                Map<String, String> response = new HashMap<>();
                response.put("message", "Código inválido, expirado ou muitas tentativas");
                response.put("status", "error");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Erro ao verificar email");
            response.put("status", "error");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerificationCode(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            boolean sent = userService.resendVerificationCode(email);
            
            if (sent) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Código reenviado com sucesso");
                response.put("status", "success");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Email já verificado ou não encontrado");
                response.put("status", "error");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Erro ao reenviar código");
            response.put("status", "error");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            boolean loggedIn = userService.loginUser(user.getEmail(), user.getSenha());
            
            if (loggedIn) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Login realizado com sucesso");
                response.put("status", "success");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Email ou senha incorretos");
                response.put("status", "error");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }
}