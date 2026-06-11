package com.example.examhero.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PatchExchange;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.examhero.entity.QuestionAttempt;
import com.example.examhero.service.QuestionAttemptService;
import com.example.examhero.dto.QuestionCardForm;
import com.example.examhero.entity.ExamCategory;
import com.example.examhero.entity.QuestionCard;
import com.example.examhero.entity.User;
import com.example.examhero.service.ExamCategoryService;
import com.example.examhero.service.QuestionCardService;

@Controller
public class QuestionController {
    
    private final QuestionCardService questionCardService;
    private final ExamCategoryService examCategoryService;
    private final QuestionAttemptService questionAttemptService;

    public QuestionController(
        QuestionCardService questionCardService,
        ExamCategoryService examCategoryService,
        QuestionAttemptService questionAttemptService, AuthController authController
    ) {
        this.questionCardService = questionCardService;
        this.examCategoryService = examCategoryService;
        this.questionAttemptService = questionAttemptService;
    }

    @GetMapping("/questions")
    public String list(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        User loginUser = questionCardService.findUserByEmail(userDetails.getUsername());
        List<QuestionCard> questionCards = questionCardService.findQuestionCardsByUser(loginUser);

        model.addAttribute("questionCards", questionCards);
        model.addAttribute("loginEmail", loginUser.getEmail());

        return "questions/list";
    }

    @GetMapping("/questions/new")
    public String newForm(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        User loginUser = questionCardService.findUserByEmail(userDetails.getUsername());
        List<ExamCategory> categories = examCategoryService.findCategoriesByUser(loginUser);

        model.addAttribute("questionCardForm", new QuestionCardForm());
        model.addAttribute("categories", categories);
        model.addAttribute("loginEmail", loginUser.getEmail());

        return "questions/form";
    }

    @GetMapping("/questions/{id}/edit")
    public String editForm(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        User loginUser = questionCardService.findUserByEmail(userDetails.getUsername());
        QuestionCard questionCard = questionCardService.findQuestionCardByIdAndUser(id, loginUser);
        QuestionCardForm form = new QuestionCardForm();

        form.setQuestionText(questionCard.getQuestionText());
        form.setOptionA(questionCard.getOptionA());
        form.setOptionB(questionCard.getOptionB());
        form.setOptionC(questionCard.getOptionC());
        form.setOptionD(questionCard.getOptionD());
        form.setCorrectAnswer(questionCard.getCorrectAnswer());
        form.setExplanation(questionCard.getExplanation());

        model.addAttribute("questionCardForm", form);
        model.addAttribute("questionCardId", id);

        return "questions/form";
    }

    @PostMapping("/questions")
    public String create(
        @Valid @ModelAttribute("questionCardForm") QuestionCardForm form,
        BindingResult bindingResult,
        @AuthenticationPrincipal UserDetails userDetails,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        User loginUser = questionCardService.findUserByEmail(userDetails.getUsername());
        List<ExamCategory> categories = examCategoryService.findCategoriesByUser(loginUser);

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categories);
            model.addAttribute("loginEmail", loginUser.getEmail());
            return "questions/form";
        }

        try {
            questionCardService.createQuestionCard(form, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "問題カードを作成しました");
            return "redirect:/questions";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("questionCardError", e.getMessage());
            model.addAttribute("categories", categories);
            model.addAttribute("loginEmail", loginUser.getEmail());
            return "questions/form";
        }
    }

    @PostMapping("/questions/{id}/edit")
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute("questionCardForm") QuestionCardForm form,
        BindingResult bindingResult,
        @AuthenticationPrincipal UserDetails userDetails,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("questionCardId", id);
            return "questions/form";
        }
        try {
            User loginUser = questionCardService.findUserByEmail(userDetails.getUsername());
            questionCardService.updateQuestionCard(form, loginUser, id);
            redirectAttributes.addFlashAttribute("successMessage", "問題を更新しました");
            return "redirect:/questions";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("questionsError", e.getMessage());
            model.addAttribute("questionCardId", id);
            return "questions/form";
        }
    }
    
    @GetMapping("/questions/{id}")
    public String detail(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        User loginUser = questionCardService.findUserByEmail(userDetails.getUsername());
        QuestionCard questionCard = questionCardService.findQuestionCardByIdAndUser(id, loginUser);

        model.addAttribute("questionCard", questionCard);
        model.addAttribute("loginEmail", loginUser.getEmail());

        return "questions/detail";
    }

    @GetMapping("/questions/categories/{categoryId}/select")
    public String selectQuestionsByCategory(
        @PathVariable Long categoryId,
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        User loginUser = questionCardService.findUserByEmail(userDetails.getUsername());
        ExamCategory category = examCategoryService.findCategoryByIdAndUser(categoryId, loginUser);
        List<QuestionCard> questionCards = questionCardService.findQuestionCardsByCategory(loginUser, categoryId);

        model.addAttribute("loginEmail", loginUser.getEmail());
        model.addAttribute("category", category);
        model.addAttribute("questionCards", questionCards);

        return "questions/select";
    }

    @GetMapping("/questions/{id}/solve")
    public String solveForm(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        User loginUser = questionCardService.findUserByEmail(userDetails.getUsername());
        QuestionCard questionCard = questionCardService.findQuestionCardByIdAndUser(id, loginUser);

        model.addAttribute("questionCard", questionCard);
        model.addAttribute("loginEmail", loginUser.getEmail());
        model.addAttribute("answered", false);
        model.addAttribute("selectedAnswer", null);
        model.addAttribute("isCorrect", null);

        return "questions/solve";
    }

    @PostMapping("/questions/{id}/solve")
    public String solveSubmit(
        @PathVariable Long id,
        @RequestParam(name = "selectedAnswer", required = false) String selectedAnswer,
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        User loginUser = questionCardService.findUserByEmail(userDetails.getUsername());
        QuestionCard questionCard = questionCardService.findQuestionCardByIdAndUser(id, loginUser);

        if (selectedAnswer == null || selectedAnswer.isBlank()) {
            model.addAttribute("questionCard", questionCard);
            model.addAttribute("loginEmail", loginUser.getEmail());
            model.addAttribute("answered", false);
            model.addAttribute("selectedAnswer", null);
            model.addAttribute("isCorrect", null);
            model.addAttribute("errorMessage", "選択肢を1つ選んでください。");

            return "questions/solve";
        }

        try {
            QuestionAttempt questionAttempt = questionAttemptService.saveAttempt(
                loginUser,
                questionCard,
                selectedAnswer
            );

            model.addAttribute("questionCard", questionCard);
            model.addAttribute("loginEmail", loginUser.getEmail());
            model.addAttribute("answered", true);
            model.addAttribute("selectedAnswer", questionAttempt.getSelectedAnswer());
            model.addAttribute("isCorrect", questionAttempt.isCorrect());

            return "questions/solve";
        } catch (IllegalArgumentException e) {
            model.addAttribute("questionCard", questionCard);
            model.addAttribute("loginEmail", loginUser.getEmail());
            model.addAttribute("answered", false);
            model.addAttribute("selectedAnswer", null);
            model.addAttribute("isCorrect", null);
            model.addAttribute("errorMessage", e.getMessage());

            return "questions/solve";
        }
    }    

    @PostMapping("questions/{id}/delete")
    public String delete(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails,
        RedirectAttributes redirectAttributes
    ) {
        User loginUser = questionCardService.findUserByEmail(userDetails.getUsername());
        questionCardService.deleteQuestionCard(loginUser, id);
        redirectAttributes.addFlashAttribute("successMessage", "問題を削除しました");
        return "questions/list";
    }
}
