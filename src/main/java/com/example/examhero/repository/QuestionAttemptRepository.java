package com.example.examhero.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.examhero.entity.QuestionAttempt;
import com.example.examhero.entity.QuestionCard;
import com.example.examhero.entity.User;

public interface QuestionAttemptRepository extends JpaRepository<QuestionAttempt, Long> {

    List<QuestionAttempt> findByUserOrderByAttemptedAtDesc(User user);

    List<QuestionAttempt> findByUserAndQuestionCardOrderByAttemptedAtDesc(
        User user,
        QuestionCard questionCard
    );

    long countByUserAndAttemptedAtGreaterThanEqualAndAttemptedAtLessThan(
        User user,
        LocalDateTime start,
        LocalDateTime end
    );

    long countByUser(User user);

    long countByUserAndCorrect(User user, boolean correct);

    void deleteByUserAndQuestionCardIn(User user, List<QuestionCard> questionCards);
}
