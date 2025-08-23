package com.ghartmann.dto;

public class EmailDTO {
    private String code;
    private String email;
    
    // Construtores
    public EmailDTO() {}
    
    public EmailDTO(String code, String email) {
        this.code = code;
        this.email = email;
    }
    
    // Getters e Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
