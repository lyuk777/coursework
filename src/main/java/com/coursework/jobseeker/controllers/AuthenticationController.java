package com.coursework.jobseeker.controllers;

import com.coursework.jobseeker.model.User;
import com.coursework.jobseeker.repository.UserRepository;
import com.coursework.jobseeker.service.InfoMessageService;
import com.coursework.jobseeker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

@Controller
public class AuthenticationController {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserService userService;
    @Autowired
    InfoMessageService messageService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() { return "logout_temp"; }


    @PostMapping("/registration")
    public String addUser(@RequestParam String username,
                          @RequestParam String email,
                          @RequestParam String password,
                          HttpServletRequest request,
                          Model model) throws IOException {
        if (userService.getUserByUsername(username)!= null) {
            messageService.createErrorMessage(model,"Пользователь с таким логином уже существует",
                    "Пользователь с логином '" + username + "' существует. Выберите другой логин.");
            return "home";
        }
        if (userService.getUserByEmail(email) != null) {
            messageService.createErrorMessage(model, "Пользователь с таким email уже существует",
                    "Пользователь с email '" + email + "' существует. Может быть у вас уже есть аккаунт.");
            return "home";
        }
        User user = userService.createUser(username, email, password);
        try {
            request.login(username, password);
        } catch (ServletException e) {
            LOGGER.error("Проблема во время авторизации ", e);
        }
        return "redirect:/home";
    }
}
