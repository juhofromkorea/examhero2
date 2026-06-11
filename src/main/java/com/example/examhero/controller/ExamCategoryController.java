package com.example.examhero.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String list( 
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam(defaultValue = "false") boolean editMode, 
        Model model
    ) {
        User loginUser = examCategoryService.findUserByEmail(userDetails.getUsername());  
        List<ExamCategory> categories = examCategoryService.findCategoriesByUser(loginUser);

        model.addAttribute("categories", categories);
        model.addAttribute("editMode", editMode);
        model.addAttribute("examCategoryForm", new ExamCategoryForm());
    
        return "categories/list";
    }

    @GetMapping("/categories/new")
    public String newForm( Model model ) {

        model.addAttribute("examCategoryForm", new ExamCategoryForm());

        return "categories/form";
    }

    @GetMapping("/categories/{id}/edit")
    public String editForm( 
        @PathVariable Long id, 
        @AuthenticationPrincipal UserDetails userDetails, 
        Model model 
    ) {
        User loginUser = examCategoryService.findUserByEmail(userDetails.getUsername());
        ExamCategory category = examCategoryService.findCategoryByIdAndUser(id, loginUser);
        ExamCategoryForm form = new ExamCategoryForm();

        form.setName(category.getName());
        form.setDescription(category.getDescription());

        model.addAttribute("examCategoryForm", form);
        model.addAttribute("categoryId", id);

        return "categories/form";
    }

    @PostMapping("/categories")
    public String create(
        @Valid @ModelAttribute("examCategoryForm") ExamCategoryForm form,
        BindingResult bindingResult,
        @AuthenticationPrincipal UserDetails userDetails,
        RedirectAttributes redirectAttributes
    ){

        if (bindingResult.hasErrors()) {
            return "categories/form";
        }
        
        try {
            User loginUser = examCategoryService.findUserByEmail(userDetails.getUsername());

            examCategoryService.createCategory(form, loginUser);    
            redirectAttributes.addFlashAttribute("successMessage", "カテゴリを作成しました");
            return "redirect:/categories";

        } catch (IllegalArgumentException e) {
            bindingResult.reject("categoryError", e.getMessage());
            return "categories/form";
        }
    }
    
    @PostMapping("/categories/{id}/edit")
    public String update( 
        @PathVariable Long id,
        @Valid @ModelAttribute("examCategoryForm") ExamCategoryForm form,
        BindingResult bindingResult,
        @AuthenticationPrincipal UserDetails userDetails,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryId", id);
            return "categories/form";
        }
        try {
            User loginUser = examCategoryService.findUserByEmail(userDetails.getUsername());
            examCategoryService.updateCategory(form, loginUser, id);
            redirectAttributes.addFlashAttribute("successMessage", "カテゴリを更新しました");
            return "redirect:/categories";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("categoryError", e.getMessage());
            model.addAttribute("categoryId", id);
            return "categories/form";
        }
    }

    @PostMapping("categories/{id}/delete")
    public String delete(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails,
        RedirectAttributes redirectAttributes
    ) {
        User loginUser = examCategoryService.findUserByEmail(userDetails.getUsername());
        examCategoryService.deleteCategory(loginUser, id);
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリを削除しました");
        return "categories/list";
    }
}