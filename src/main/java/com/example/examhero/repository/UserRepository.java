package com.example.examhero.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.examhero.entity.User;





    public interface UserRepository extends JpaRepository<User, Long> {


        Optional<User> findByEmail(Long email);

        boolean exexistsByEmail(String email);
    }

