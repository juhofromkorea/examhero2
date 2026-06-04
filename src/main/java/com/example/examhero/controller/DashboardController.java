package com.example.examhero.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.examhero.entity.ExamCategory;
import com.example.examhero.entity.User;
import com.example.examhero.service.ExamCategoryService;

@Controller
public class DashboardController {

 private final ExamCategoryService examCategoryService;

    public DashboardController(ExamCategoryService examCategoryService) {
        this.examCategoryService = examCategoryService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal){

        String loginEmail = principal.getName();

        User loginUser = examCategoryService.findUserByEmail(loginEmail);

        List<ExamCategory> categories = examCategoryService.findCategoriesByUser(loginUser);

        long categoryCount = examCategoryService.countCategoriesByUser(loginUser);

        model.addAttribute("loginEmail", loginEmail);

        model.addAttribute("loginEmail", loginEmail);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryCount", categoryCount);;






        return "dashboard";
    }
}
