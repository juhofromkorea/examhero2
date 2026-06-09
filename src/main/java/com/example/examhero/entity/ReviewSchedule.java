package com.example.examhero.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "review_schedules")
public class ReviewSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_card_id", nullable = false, unique = true)
    private QuestionCard questionCard;

    @Column(nullable = false)
    private LocalDate nextReviewDate;

    @Column(nullable = false)
    private int intervalDays;

    @Column(nullable = false)
    private int reviewCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewDifficulty lastDifficulty;

    private LocalDateTime lastReviewedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public LocalDate getNextReviewDate() {
        return nextReviewDate;
    }

    public int getIntervalDays() {
        return intervalDays;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public ReviewDifficulty getLastDifficulty() {
        return lastDifficulty;
    }

    public LocalDateTime getLastReviewedAt() {
        return lastReviewedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuestionCard(QuestionCard questionCard) {
        this.questionCard = questionCard;
    }

    public void setNextReviewDate(LocalDate nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public void setIntervalDays(int intervalDays) {
        this.intervalDays = intervalDays;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public void setLastDifficulty(ReviewDifficulty lastDifficulty) {
        this.lastDifficulty = lastDifficulty;
    }

    public void setLastReviewedAt(LocalDateTime lastReviewedAt) {
        this.lastReviewedAt = lastReviewedAt;
    }
}