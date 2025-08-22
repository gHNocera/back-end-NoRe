package com.ghartmann.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ghartmann.dao.IUserDAO;
import com.ghartmann.dao.UserDAO;
import com.ghartmann.domain.User;

@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserDAO userDAO;

    public UserController() {
        this.userDAO = new UserDAO();
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (userDAO.registerUser(user)) {
            return "User registered successfully";
        } else {
            return "Error registering user";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String userName, @RequestParam String password) {
        if (userDAO.loginUser(userName, password)) {
            return "Login successful";
        } else {
            return "Invalid username or password";
        }
    }

}
