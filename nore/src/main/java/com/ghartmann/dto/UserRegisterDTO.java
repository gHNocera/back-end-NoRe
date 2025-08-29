package com.ghartmann.dto;


public class UserRegisterDTO {

    private Integer id;
    private String username;
    private String email;
    private String nome;
    private Long nascimento;
    private String senha;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Long getNascimento() { return nascimento; }
    public void setNascimento(Long nascimento) { this.nascimento = nascimento; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }


}
