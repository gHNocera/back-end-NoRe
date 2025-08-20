package com.ghartmann;


import org.junit.Test;

import com.ghartmann.dao.IUserDAO;
import com.ghartmann.dao.UserDAO;
import com.ghartmann.domain.User;

/**
 * Unit test for simple App.
 */
public class AppTest {
    IUserDAO userDAO = new UserDAO();
    
    @Test
    public void registerUserTest(){
        User user = new User("testUser", "testEmail", "testPassword");
        userDAO.registerUser(user);
    }

    @Test
    public void getUserByIdTest(){
        User user = new User("testUser", "testEmail", "testPassword");
        userDAO.registerUser(user);
        User retrievedUser = userDAO.getUserById(user.getId());
        assert retrievedUser != null;
    }

    @Test
    public void updateUserTest(){
        User user = new User("testUser", "testEmail", "testPassword");
        userDAO.registerUser(user);
        
        user.setUserName("updatedUser");
        user.setEmail("updatedEmail");
        user.setPassword("updatedPassword");
        
        boolean isUpdated = userDAO.updateUser(user);
        assert isUpdated;
        
        User updatedUser = userDAO.getUserById(user.getId());
        assert updatedUser.getUserName().equals("updatedUser");
    }

    @Test
    public void deleteUserTest(){
        User user = new User("testUser", "testEmail", "testPassword");
        userDAO.registerUser(user);
        
        boolean isDeleted = userDAO.deleteUser(user.getId());
        assert isDeleted;
        
        User deletedUser = userDAO.getUserById(user.getId());
        assert deletedUser == null;
    }
}
