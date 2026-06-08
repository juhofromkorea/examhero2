package com.example.examhero.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "question_attempts")
public class QuestionAttempt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_card_id", nullable = false)
    private QuestionCard questionCard;

    @Column(nullable = false, length = 1)
    private String selectedAnswer;

    @Column(name = "is_correct", nullable = false)
    private boolean correct;

    @Column(nullable = false)
    private LocalDateTime attemptedAt;

    public QuestionAttempt() {
    }

    public QuestionAttempt(
            User user,
            QuestionCard questionCard,
            String selectedAnswer,
            boolean correct
    ) {
        this.user = user;
        this.questionCard = questionCard;
        this.selectedAnswer = selectedAnswer;
        this.correct = correct;
    }

    @PrePersist
    public void onCreate() {
        this.attemptedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public QuestionCard getQuestionCard() {
        return questionCard;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }
}
