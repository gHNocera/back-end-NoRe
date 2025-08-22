package com.ghartmann.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ghartmann.PasswordUtil;
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
        String hash = new PasswordUtil().hashPassword(user.getPassword());
        user.setPasswordHash(hash);
        if (userDAO.registerUser(user)) {
            return "Usuário cadastrado com sucesso";
        } else {
            return "Erro ao cadastrar usuário. Tente novamente.";
        }
    }

    @PostMapping("/login")
public String login(@RequestBody User user) {
    if (userDAO.loginUser(user.getEmail(), user.getPassword())) {
        return "Login realizado com sucesso";
    } else {
        return "Email ou senha incorretas. Tente novamente.";
    }
}

}
