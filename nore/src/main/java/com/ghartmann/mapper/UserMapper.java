package com.ghartmann.mapper;

import com.ghartmann.domain.User;
import com.ghartmann.dto.UserRegisterDTO;
import com.ghartmann.dto.UserResponseDTO;

import java.time.Instant;

public class UserMapper {

    private UserMapper() {}

    /**
     * Converte UserRegisterDTO → User (para persistência)
     * @param dto DTO de registro
     * @param passwordHash senha já hashada
     * @return User
     */
    public static User toEntity(UserRegisterDTO dto, String passwordHash) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPasswordHash(passwordHash);
        user.setEmailVerified(false);
        user.setCodeAttempts(0);

        if (dto.getNascimento() != null) {
            user.setNascimento(Instant.ofEpochMilli(dto.getNascimento()));
        }

        return user;
    }

    /**
     * Converte User → UserResponseDTO (para enviar ao front)
     * @param user entidade User
     * @return UserResponseDTO
     */
    public static UserResponseDTO toDTO(User user) {
        if (user == null) return null;

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setEmailVerified(user.isEmailVerified());

        if (user.getNascimento() != null) {
            dto.setNascimento(user.getNascimento().toEpochMilli());
        }

        return dto;
    }
    public static void updateEntity(User user, UserRegisterDTO dto, String passwordHash) {
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getNascimento() != null) user.setNascimento(Instant.ofEpochMilli(dto.getNascimento()));
        if (passwordHash != null) user.setPasswordHash(passwordHash);
    }
}
