package com.ghartmann.dao;

import com.ghartmann.domain.User;

public interface IUserDAO {

    boolean registerUser(User user);
    User getUserById(int userId);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
    boolean loginUser(String userName, String password);
    User getUserByVerificationCode(String code);
    User getUserByEmail(String email);
    boolean verifyUserEmail(String token);
    boolean incrementVerificationAttempts(String email);
}
