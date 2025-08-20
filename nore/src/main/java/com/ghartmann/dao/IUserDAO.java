package com.ghartmann.dao;

import com.ghartmann.domain.User;

public interface IUserDAO {

    boolean registerUser(User user);
    User getUserById(int userId);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
}
