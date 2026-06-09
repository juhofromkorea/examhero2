package com.example.examhero.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.examhero.entity.QuestionCard;
import com.example.examhero.entity.ReviewDifficulty;
import com.example.examhero.entity.ReviewSchedule;
import com.example.examhero.entity.User;
import com.example.examhero.repository.QuestionCardRepository;
import com.example.examhero.repository.ReviewScheduleRepository;

@Service
public class ReviewScheduleService {
    
    private final ReviewScheduleRepository reviewScheduleRepository;
    private final QuestionCardRepository questionCardRepository;

    public ReviewScheduleService(
        ReviewScheduleRepository reviewScheduleRepository,
        QuestionCardRepository questionCardRepository
    ) {
        this.reviewScheduleRepository = reviewScheduleRepository;
        this.questionCardRepository = questionCardRepository;
    }

    @Transactional(readOnly = true)
    public List<ReviewSchedule> findTodayReviews(User user) {
        LocalDate today = LocalDate.now();
        return reviewScheduleRepository
            .findByUserAndNextReviewDateLessThanEqualOrderByNextReviewDateAsc(user, today);
    }

    @Transactional(readOnly = true)
    public long countTodayReviews(User user) {
        LocalDate today = LocalDate.now();
        return reviewScheduleRepository
            .countByUserAndNextReviewDateLessThanEqual(user, today);
    }

    @Transactional
    public void updateReviewSchedule(
        Long questionCardId,
        User user,
        ReviewDifficulty difficulty
    ) {
        QuestionCard questionCard = questionCardRepository.findById(questionCardId)
            .orElseThrow(() -> new IllegalArgumentException("問題カードが見つかりません"));

        if(!questionCard.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("この問題カードを操作する権限がありません");
        }

        ReviewSchedule schedule = reviewScheduleRepository.findByQuestionCard(questionCard)
            .orElseGet(() -> {
                ReviewSchedule newSchedule = new ReviewSchedule();
                newSchedule.setUser(user);
                newSchedule.setQuestionCard(questionCard);
                newSchedule.setIntervalDays(0);
                newSchedule.setReviewCount(0);
                return newSchedule;
            });
        
        int nextIntervalDays = calculateNextIntervalDays(
            schedule.getIntervalDays(),
            difficulty
        );

        LocalDate nextReviewDate = LocalDate.now().plusDays(nextIntervalDays);

        schedule.setIntervalDays(nextIntervalDays);
        schedule.setNextReviewDate(nextReviewDate);
        schedule.setReviewCount(schedule.getReviewCount() + 1);
        schedule.setLastDifficulty(difficulty);
        schedule.setLastReviewedAt(LocalDateTime.now());

        reviewScheduleRepository.save(schedule);
    }

    private int calculateNextIntervalDays(
        int currentIntervalDays,
        ReviewDifficulty difficulty
    ) {
        int baseInterval = Math.max(currentIntervalDays, 1);

        return switch (difficulty) {
            case AGAIN -> 1;

            case HARD -> Math.max(1, baseInterval);

            case GOOD -> {
                if (currentIntervalDays == 0) {
                    yield 3;
                }
                yield baseInterval * 2;
            }

            case EASY -> {
                if (currentIntervalDays == 0) {
                    yield 5;
                }
                yield baseInterval * 3;
            }
        };
    }
}
