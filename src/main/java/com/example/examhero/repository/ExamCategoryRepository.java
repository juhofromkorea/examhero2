package com.example.examhero.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.examhero.entity.ExamCategory;
import com.example.examhero.entity.User;

public interface ExamCategoryRepository extends JpaRepository<ExamCategory, Long> {

    List<ExamCategory> findByUserOrderByCreatedAtDesc(User user);

    boolean existsByUserAndName(User user, String name);

    boolean existsByUserAndNameAndIdNot(User user, String name, Long id);

    Optional<ExamCategory> findByIdAndUser(Long id, User user);

    long countByUser(User user);
}
