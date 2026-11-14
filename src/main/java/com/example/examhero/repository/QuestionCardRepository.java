package com.example.examhero.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.examhero.entity.ExamCategory;
import com.example.examhero.entity.QuestionCard;
import com.example.examhero.entity.User;

public interface QuestionCardRepository extends JpaRepository<QuestionCard, Long> {

    List<QuestionCard> findByUserOrderByCreatedAtDesc(User user);

    List<QuestionCard> findByUserAndExamCategoryOrderByCreatedAtDesc(
        User user, 
        ExamCategory examCategory
    );

    Optional<QuestionCard> findByIdAndUser(Long id, User user);

    long countByUserAndExamCategory(
        User user, 
        ExamCategory examCategory
    );

    long countByUser(User user);
}
