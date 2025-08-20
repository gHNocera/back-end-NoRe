package com.ghartmann;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ghartmann.dao.IUserDAO;
import com.ghartmann.dao.UserDAO;
import com.ghartmann.domain.User;

/**
 * Unit test for simple App.
 */
public class AppTest {
    
    @Test
    public void registerUserTest(){
        User user = new User("testUser", "testPassword", "testEmail");
        IUserDAO userDAO = new UserDAO();
        userDAO.registerUser(user);
    }
}
