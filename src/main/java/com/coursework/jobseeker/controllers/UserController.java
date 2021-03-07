package com.coursework.jobseeker.controllers;

import com.coursework.jobseeker.model.User;
import com.coursework.jobseeker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    private void addAttributes(User user, Model model){
        model.addAttribute("surname",user.getSurname());
        model.addAttribute("name",user.getName());
        model.addAttribute("patronymic",user.getPatronymic());
        model.addAttribute("city",user.getCity());
        model.addAttribute("phone",user.getPhoneNumber());
        model.addAttribute("about",user.getAbout());
    }

    // страница профиля
    @GetMapping("/")
    public String profilePage(@AuthenticationPrincipal User user, Model model)
    {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("initials",userService.getInitials(user));
        model.addAttribute("user", user);
        addAttributes(user, model);
        return "/user/profile";
    }

    // страница редактирования профиля
    @GetMapping("/edit")
    public String editUser(@AuthenticationPrincipal User user, Model model)
    {
        addAttributes(user, model);
        return "/user/edit-profile";
    }

    // отредактировать профиль
    @PostMapping("/edit")
    public String editProfile(@RequestParam String name,
                             @RequestParam String surname,
                             @RequestParam String patronymic,
                             @RequestParam String city,
                             @RequestParam String phone,
                             @RequestParam String about,
                             @AuthenticationPrincipal User user) {
        userService.editProfile(name, surname, patronymic, city, phone, about, user);
        return "redirect:/user/";
    }
}
