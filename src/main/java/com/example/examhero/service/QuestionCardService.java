package com.example.examhero.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.examhero.dto.QuestionCardForm;
import com.example.examhero.entity.ExamCategory;
import com.example.examhero.entity.QuestionCard;
import com.example.examhero.entity.User;
import com.example.examhero.repository.ExamCategoryRepository;
import com.example.examhero.repository.QuestionCardRepository;
import com.example.examhero.repository.UserRepository;

@Service
public class QuestionCardService {
    
    private final QuestionCardRepository questionCardRepository;

    private final ExamCategoryRepository examCategoryRepository;

    private final UserRepository userRepository;

    public QuestionCardService(
            QuestionCardRepository questionCardRepository,
            ExamCategoryRepository examCategoryRepository,
            UserRepository userRepository
    ) {
        this.questionCardRepository = questionCardRepository;
        this.examCategoryRepository = examCategoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("ログイン中のユーザーが見つかりません"));
    }

    @Transactional(readOnly = true)
    public List<QuestionCard> findQuestionCardsByUser(User user) {
        return questionCardRepository.findByUserOrderByCreatedAtDesc(user);
    }


    @Transactional(readOnly = true)
    public List<QuestionCard> findQuestionCardsByCategory(User user, Long examCategoryId) {

        ExamCategory examCategory = findCategoryByIdAndUser(examCategoryId, user);

        return questionCardRepository.findByUserAndExamCategoryOrderByCreatedAtDesc(user,examCategory);
    }     
    
    @Transactional(readOnly = true)
    public QuestionCard findQuestionCardByIdAndUser(Long id, User user) {
        return questionCardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("問題カードが見つかりません"));
    }

     @Transactional
    public QuestionCard createQuestionCard(QuestionCardForm form, User user) {

        ExamCategory examCategory = findCategoryByIdAndUser(form.getExamCategoryId(), user);

        String questionText = form.getQuestionText().trim();
        String optionA = form.getOptionA().trim();
        String optionB = form.getOptionB().trim();
        String optionC = form.getOptionC().trim();
        String optionD = form.getOptionD().trim();
        String correctAnswer = form.getCorrectAnswer().trim();

        String explanation = null;

        if (form.getExplanation() != null && !form.getExplanation().trim().isEmpty()) {
            explanation = form.getExplanation().trim();
        }

                QuestionCard questionCard = new QuestionCard(
                questionText,
                optionA,
                optionB,
                optionC,
                optionD,
                correctAnswer,
                explanation,
                user,
                examCategory
        );
        return questionCardRepository.save(questionCard);
    }

     private ExamCategory findCategoryByIdAndUser(Long examCategoryId, User user) {
        return examCategoryRepository.findByIdAndUser(examCategoryId, user)
                .orElseThrow(() -> new IllegalArgumentException("カテゴリが見つかりません"));
    }

    @Transactional(readOnly = true)
    public long countQuestionCardsByUser(User user) {
        return questionCardRepository.countByUser(user);
    }
}
