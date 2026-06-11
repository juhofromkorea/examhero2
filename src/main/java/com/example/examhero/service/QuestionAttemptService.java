package com.example.examhero.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examhero.entity.QuestionAttempt;
import com.example.examhero.entity.QuestionCard;
import com.example.examhero.entity.User;
import com.example.examhero.repository.QuestionAttemptRepository;


@Service
public class QuestionAttemptService {

    private final QuestionAttemptRepository questionAttemptRepository;


    public QuestionAttemptService(QuestionAttemptRepository questionAttemptRepository) {
        this.questionAttemptRepository = questionAttemptRepository;
    }


    @Transactional
    public QuestionAttempt saveAttempt(
            User user,
            QuestionCard questionCard,
            String selectedAnswer
    ) {

        if (selectedAnswer == null || selectedAnswer.isBlank()) {
            throw new IllegalArgumentException("選択肢を1つ選んでください。");
        }

        String normalizedSelectedAnswer = selectedAnswer.trim().toUpperCase();

        if (!isValidAnswer(normalizedSelectedAnswer)) {
            throw new IllegalArgumentException("選択肢は A, B, C, D のいずれかを選んでください。");
        }


        boolean correct = normalizedSelectedAnswer.equals(questionCard.getCorrectAnswer());


        QuestionAttempt questionAttempt = new QuestionAttempt(
                user,
                questionCard,
                normalizedSelectedAnswer,
                correct
        );

        return questionAttemptRepository.save(questionAttempt);
    }

    @Transactional(readOnly = true)
    public long countTodayAttempts(User user) {

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime startOfTomorrow = startOfToday.plusDays(1);

        return questionAttemptRepository
                .countByUserAndAttemptedAtGreaterThanEqualAndAttemptedAtLessThan(
                        user,
                        startOfToday,
                        startOfTomorrow
                );
    }

    @Transactional(readOnly = true)
    public long countAllAttempts(User user) {
        return questionAttemptRepository.countByUser(user);
    }

    @Transactional(readOnly = true)
    public long countCorrectAttempts(User user) {
        return questionAttemptRepository.countByUserAndCorrect(user, true);
    }

    private boolean isValidAnswer(String answer) {
        return "A".equals(answer)
                || "B".equals(answer)
                || "C".equals(answer)
                || "D".equals(answer);
    }
}