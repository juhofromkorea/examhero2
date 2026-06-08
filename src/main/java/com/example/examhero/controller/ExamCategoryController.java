package com.example.examhero.controller;

import java.security.Principal;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/categories")
    public String ExamCategory( @AuthenticationPrincipal UserDetails userDetails,
            Model model, Principal principal){
        String email = principal.getName();
        User user = examCategoryService.findUserByEmail(email);     
        
        List<ExamCategory> categories = examCategoryService.findCategoriesByUser(user);
        model.addAttribute("categories", categories);

        model.addAttribute("examCategoryForm", new ExamCategoryForm());
    

        return "categories/list";
    }

    @GetMapping("/categories/new")
    public String newform(Model model) {

        model.addAttribute("examCategoryForm", new ExamCategoryForm());

        return "categories/form";
    }

    @PostMapping("/categories")
    public String create(
        @Valid @ModelAttribute("examCategoryForm") ExamCategoryForm form,
        BindingResult bindingResult,
        @AuthenticationPrincipal UserDetails userDetails,
        RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            return "categories/form";
        }
        
        try{
            User loginUser = examCategoryService.findUserByEmail(userDetails.getUsername());

            examCategoryService.createCategory(form, loginUser);    
            redirectAttributes.addFlashAttribute("successMessage", "カテゴリを作成しました");
            return "redirect:/categories";

        } catch (IllegalArgumentException e){
            bindingResult.reject("categoryError", e.getMessage());
            return "categories/form";
        }
    }
        
}