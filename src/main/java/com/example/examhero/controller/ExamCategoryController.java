package com.example.examhero.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.examhero.dto.ExamCategoryForm;
import com.example.examhero.entity.ExamCategory;
import com.example.examhero.entity.User;
import com.example.examhero.service.ExamCategoryService;

@Controller
public class ExamCategoryController {

    private final ExamCategoryService examCategoryService;

    public ExamCategoryController(ExamCategoryService examCategoryService) {
        this.examCategoryService = examCategoryService;
    }

    @GetMapping("/ExamCategory")
    public String ExamCategory(Model model, Principal principal){

        String email = principal.getName();
        User user = examCategoryService.findUserByEmail(email);

        // ② そのお客さんの「カテゴリ一覧」をシェフにもらって、お盆に乗せる
        List<ExamCategory> categories = examCategoryService.findCategoriesByUser(user);
        model.addAttribute("categories", categories);

        // ③ 新しいカテゴリを作るための「真っ白な注文票」をお盆に乗せる
        model.addAttribute("examCategoryForm", new ExamCategoryForm());
    

        return "ExamCategory";
    }
}
