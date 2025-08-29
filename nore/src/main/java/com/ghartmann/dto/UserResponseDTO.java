package com.ghartmann.dto;

public class UserResponseDTO {

    private Integer id;
    private String username;
    private String email;
    private String nome;
    private Long nascimento; 
    private boolean emailVerified;

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

    public String getName() {
        return nome;
    }

    public void setName(String nome) {
        this.nome = nome;
    }

    public Long getNascimento() {
        return nascimento;
    }

    public void setNascimento(Long nascimento) {
        this.nascimento = nascimento;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
}
