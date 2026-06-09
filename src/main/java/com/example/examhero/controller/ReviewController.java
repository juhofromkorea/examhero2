package com.example.examhero.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.examhero.entity.ReviewDifficulty;
import com.example.examhero.entity.ReviewSchedule;
import com.example.examhero.entity.User;
import com.example.examhero.service.ReviewScheduleService;
import com.example.examhero.service.UserService;

@Controller
public class ReviewController {

    private final ReviewScheduleService reviewScheduleService;
    private final UserService userService;

    public ReviewController(
        ReviewScheduleService reviewScheduleService,
        UserService userService
    ) {
        this.reviewScheduleService = reviewScheduleService;
        this.userService = userService;
    }

    @GetMapping("/reviews")
    public String listTodayReviews(
        Principal principal,
        Model model
    ) {
        User user = userService.findByEmail(principal.getName());

        List<ReviewSchedule> reviews = reviewScheduleService.findTodayReviews(user);

        model.addAttribute("reviews", reviews);

        return "reviews/list";
    }

    @PostMapping("/reviews/schedule")
    public String updateReviewSchedule(
        @RequestParam Long questionCardId,
        @RequestParam ReviewDifficulty difficulty,
        Principal principal
    ) {
        User user = userService.findByEmail(principal.getName());

        reviewScheduleService.updateReviewSchedule(
            questionCardId,
            user,
            difficulty
        );

        return "redirect:/reviews";
    }
}