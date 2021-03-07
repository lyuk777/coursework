package com.coursework.jobseeker.controllers;

import com.coursework.jobseeker.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {
            return "redirect:/home";
        }
        return "home";
    }

    // отображение стартовой страницы
    @GetMapping("/home")
    public String mainPage(@AuthenticationPrincipal User user, Model model)
    {
        return "main";
    }
}
