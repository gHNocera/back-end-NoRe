package com.ghartmann.dao;

import com.ghartmann.domain.User;

public class UserDAO implements IUserDAO {


    @Override
    public boolean registerUser(User user) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public User getUserById(int userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }

    @Override
    public boolean updateUser(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public boolean deleteUser(int userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }


}
