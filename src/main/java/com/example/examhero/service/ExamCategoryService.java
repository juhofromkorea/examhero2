package com.example.examhero.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examhero.dto.ExamCategoryForm;
import com.example.examhero.entity.ExamCategory;
import com.example.examhero.entity.QuestionCard;
import com.example.examhero.entity.User;
import com.example.examhero.repository.ExamCategoryRepository;
import com.example.examhero.repository.UserRepository;
import com.example.examhero.repository.QuestionAttemptRepository;
import com.example.examhero.repository.QuestionCardRepository;

@Service
public class ExamCategoryService {
    
    private final QuestionAttemptRepository questionAttemptRepository;
    private final QuestionCardRepository questionCardRepository;
    private final ExamCategoryRepository examCategoryRepository;
    private final UserRepository userRepository;

    public ExamCategoryService(
        ExamCategoryRepository examCategoryRepository,
        UserRepository userRepository, QuestionCardRepository questionCardRepository, QuestionAttemptRepository questionAttemptRepository
    ) {
            this.examCategoryRepository = examCategoryRepository;
            this.userRepository = userRepository;
            this.questionCardRepository = questionCardRepository;
            this.questionAttemptRepository = questionAttemptRepository;
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).
            orElseThrow(() -> new IllegalArgumentException("ログイン中のユーザーが見つかりません"));
    }

    @Transactional(readOnly = true)
    public List<ExamCategory> findCategoriesByUser(User user) {
        return examCategoryRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public ExamCategory createCategory(ExamCategoryForm form, User user) {
        String trimmedName = form.getName().trim();
        String trimmedDescription = null;

        if (form.getDescription() != null) {
            trimmedDescription = form.getDescription().trim();
        }

        if (examCategoryRepository.existsByUserAndName(user, trimmedName)) {
            throw new IllegalArgumentException("同じ名前のカテゴリがすでに存在します");
        }

        ExamCategory category = new ExamCategory(trimmedName, trimmedDescription, user);

        return examCategoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(ExamCategoryForm form, User user, Long id) {
        ExamCategory category = findCategoryByIdAndUser(id, user);
        String trimmedName = form.getName().trim();
        String trimmedDescription = null;

        if (form.getDescription() != null) {
            trimmedDescription = form.getDescription().trim();
        }

        if (examCategoryRepository.existsByUserAndNameAndIdNot(user, trimmedName, id)) {
            throw new IllegalArgumentException("同じ名前のカテゴリがすでに存在します");
        }

        category.setName(trimmedName);
        category.setDescription(trimmedDescription);
    }

    @Transactional
    public long deleteCategoryWithQuestions(User user, Long id) {
        ExamCategory category = findCategoryByIdAndUser(id, user);
        List<QuestionCard> questionCards = questionCardRepository.findByUserAndExamCategoryOrderByCreatedAtDesc(user, category);
        long deletedQuestionCount = questionCards.size();

        if (!questionCards.isEmpty()) {
            questionAttemptRepository.deleteByUserAndQuestionCardIn(user, questionCards);
            questionCardRepository.deleteAll(questionCards);
        }
        
        examCategoryRepository.delete(category);

        return deletedQuestionCount;
    }

    @Transactional(readOnly = true)
    public ExamCategory findCategoryByIdAndUser(Long id, User user) {
        return examCategoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("カテゴリが見つかりません"));
    }

    @Transactional(readOnly = true)
    public long countCategoriesByUser(User user) {
        return examCategoryRepository.countByUser(user);
    }

    @Transactional(readOnly = true)
    public long countQuestionCardsByCategory(User user, Long categoryId) {
        ExamCategory category = findCategoryByIdAndUser(categoryId, user);
        return questionCardRepository.countByUserAndExamCategory(user, category);
    }
}
