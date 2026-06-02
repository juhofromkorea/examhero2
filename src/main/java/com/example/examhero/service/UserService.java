package com.example.examhero.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.examhero.dto.SignupForm;
import com.example.examhero.entity.User;
import com.example.examhero.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public user register(SignupForm form) {
        if (userRepository.existByEmail(form.getEmail())) {
            throw new IllegalArgumentException("このメールアドレスは既に登録されています");
        }

        String encodedPassword = passwordEncoder.encode(form.getPassword());

        User user = new User(form.getUsername(), form.getEmail(), encodedPassword);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean existByEmail(String email){
        return userRepository.existByEmail(email);
    }
}
