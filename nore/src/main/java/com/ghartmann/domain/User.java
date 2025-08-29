package com.ghartmann.domain;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "user_table") // Recomendo usar nome em lowercase
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_user_sequence")
    @SequenceGenerator(name = "id_user_sequence", sequenceName = "user_seq", allocationSize = 1)
    @Column(name = "id")
    private Integer id; 

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email_verified")
    private boolean emailVerified = false;

    @Column(name = "verification_code", length = 6)
    private String verificationCode;

    @Column(name = "code_expiry_date")
    private LocalDateTime codeExpiryDate;

    @Column(name = "code_attempts")
    private int codeAttempts = 0;

    @Transient
    private String senha;

    @Column(name = "nascimento", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant nascimento;

    @Column(name = "password_hash", nullable = false) 
    private String passwordHash;

    public User() {
        // Default constructor for JPA
    }

    public User(String nome, String username, String email, String senha) {
        this.nome = nome;
        this.username = username;
        this.email = email;
        this.senha = senha;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Instant getNascimento() {
        return nascimento;
    }

    public void setNascimento(Instant nascimento) {
        this.nascimento = nascimento;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    
    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public LocalDateTime getCodeExpiryDate() {
        return codeExpiryDate;
    }

    public void setCodeExpiryDate(LocalDateTime codeExpiryDate) {
        this.codeExpiryDate = codeExpiryDate;
    }

    public int getCodeAttempts() {
        return codeAttempts;
    }

    public void setCodeAttempts(int codeAttempts) {
        this.codeAttempts = codeAttempts;
    }

    public void incrementCodeAttempts() {
        this.codeAttempts++;
    }

}