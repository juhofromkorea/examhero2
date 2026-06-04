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
        return "dashboard";
    }
}
