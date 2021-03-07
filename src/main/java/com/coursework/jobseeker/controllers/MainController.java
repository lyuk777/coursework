package com.coursework.jobseeker.controllers;

import com.coursework.jobseeker.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    // главная страница
    @GetMapping("/main")
    public String showMain(@AuthenticationPrincipal User user){
        return "main";
    }
}
