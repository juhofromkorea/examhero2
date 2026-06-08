package com.example.examhero.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.examhero.entity.ExamCategory;
import com.example.examhero.entity.User;
import com.example.examhero.service.ExamCategoryService;
import com.example.examhero.service.QuestionAttemptService;
import com.example.examhero.service.QuestionCardService;


@Controller
public class DashboardController {

    private final ExamCategoryService examCategoryService;

    private final QuestionCardService questionCardService;


    private final QuestionAttemptService questionAttemptService;


    public DashboardController(
            ExamCategoryService examCategoryService,
            QuestionCardService questionCardService,
            QuestionAttemptService questionAttemptService
    ) {
        this.examCategoryService = examCategoryService;
        this.questionCardService = questionCardService;
        this.questionAttemptService = questionAttemptService;
    }


    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {


        String loginEmail = principal.getName();

        User loginUser = examCategoryService.findUserByEmail(loginEmail);


        List<ExamCategory> categories = examCategoryService.findCategoriesByUser(loginUser);


        long categoryCount = examCategoryService.countCategoriesByUser(loginUser);
        long questionCount = questionCardService.countQuestionCardsByUser(loginUser);
        long todayAttemptCount = questionAttemptService.countTodayAttempts(loginUser);


        model.addAttribute("loginEmail", loginEmail);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryCount", categoryCount);
        model.addAttribute("questionCount", questionCount);
        model.addAttribute("todayReviewCount", todayAttemptCount);

        return "dashboard";
    }
}
