package com.example.examhero.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.examhero.dto.SignupForm;
import com.example.examhero.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("signupForm", new SignupForm());

        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(
        @Valid SignupForm signupForm,
        BindingResult bindingResult,
        Model model) {
        
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        try {
            //userService.register(signupForm);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());

            return "auth/signup";
        }
        
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }
}
