package com.ghartmann;


import org.junit.Test;

import com.ghartmann.dao.IUserDAO;
import com.ghartmann.dao.UserDAO;
import com.ghartmann.domain.User;
import com.ghartmann.validations.UserValidations;

/**
 * Unit test for simple App.
 */
public class AppTest {

     IUserDAO userDAO = new UserDAO();
     UserValidations userValidations = new UserValidations();
     PasswordUtil passwordUtil = new PasswordUtil();
    
    @Test
    public void registerUserTest(){
        User user = registerUser();
        assert user != null;
    }

    @Test
    public void getUserByIdTest(){
        User user = registerUser();
        User retrievedUser = userDAO.getUserById(user.getId());
        assert retrievedUser != null;
    }

    @Test
    public void updateUserTest(){
        User user = registerUser();
        
        user.setUserName("updatedUser");
        user.setEmail("updatedEmail@test.com");
        user.setPassword("updatedPassword");
        
        boolean isUpdated = userDAO.updateUser(user);
        assert isUpdated;
        
        User updatedUser = userDAO.getUserById(user.getId());
        assert updatedUser.getUserName().equals("updatedUser");
    }

    @Test
    public void deleteUserTest(){
        User user = registerUser();
        
        boolean isDeleted = userDAO.deleteUser(user.getId());
        assert isDeleted;
        
        User deletedUser = userDAO.getUserById(user.getId());
        assert deletedUser == null;
    }

    @Test
    public void loginUserTest(){
        User user = registerUser();
        
        boolean isLoggedIn = userDAO.loginUser(user.getUserName(), user.getPassword());
        assert isLoggedIn;
        
        boolean isLoginFailed = userDAO.loginUser("testUser", "wrongPassword");
        assert !isLoginFailed;
    }

    public User registerUser(){
        User user = new User("testUser", "testEmail@test.com", "testPassword");
        userValidations.validateUserName(user.getUserName());
        userValidations.validateEmail(user.getEmail());
        userValidations.validatePassword(user.getPassword());
        user.setPasswordHash(passwordUtil.hashPassword(user.getPassword()));
        userDAO.registerUser(user);
        return user;
    }
}
