package com.example.examhero.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examhero.dto.ExamCategoryForm;
import com.example.examhero.entity.ExamCategory;
import com.example.examhero.entity.User;
import com.example.examhero.repository.ExamCategoryRepository;
import com.example.examhero.repository.UserRepository;

@Service
public class ExamCategoryService {
    
    private final ExamCategoryRepository examCategoryRepository;
    private final UserRepository userRepository;

    public ExamCategoryService(
        ExamCategoryRepository examCategoryRepository,
        UserRepository userRepository
    ) {
            this.examCategoryRepository = examCategoryRepository;
            this.userRepository = userRepository;
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

    @Transactional(readOnly = true)
    public ExamCategory findCategoryByIdAndUser(Long id, User user) {
        return examCategoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("カテゴリが見つかりません"));
    }

    @Transactional(readOnly = true)
    public long countCategoriesByUser(User user) {
        return examCategoryRepository.countByUser(user);
    }
}
