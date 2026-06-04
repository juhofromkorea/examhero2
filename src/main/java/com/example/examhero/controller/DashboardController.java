package com.example.examhero.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    public DashboardController() {

    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal){

        String loginEmail = principal.getName();

        model.addAttribute("loginEmail", loginEmail);

        model.addAttribute("categoryCount", 0);
        model.addAttribute("questionCount", 0);
        model.addAttribute("todayReviewCount", 0);






        return "dashboard";
    }
}
