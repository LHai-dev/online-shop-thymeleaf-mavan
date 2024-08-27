package com.onlineshopthymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping(value = "login")
    public String login(Model model){
        model.addAttribute("loginError", "Wrong user or password");
        return "auth/auth-form";
    }

    @GetMapping(value = "logout")
    public String logout(Model model){
        model.addAttribute("logoutError", "You have been logged out");
        return "auth/auth-form";
    }

}
