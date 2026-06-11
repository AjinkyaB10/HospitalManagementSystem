package com.hospital.controller;

import com.hospital.model.Role;
import com.hospital.model.User;
import com.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Home page
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Register page
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Register submit
    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute User user, Model model) {

        // Check if email already exists
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already registered!");
            return "register";
        }

        // Default role is PATIENT
        user.setRole(Role.PATIENT);
        userService.saveUser(user);

        return "redirect:/login?registered";
    }

}
