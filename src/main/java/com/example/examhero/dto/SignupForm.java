package com.example.examhero.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupForm {
    @NotBlank(message = "ユーザー名を入力してください")
    @Size(max = 50, message = "ユーザー名は50文字以内で入力してください")
    private String username;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "メールアドレスの形式で入力してください")
    @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
    private String email;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, max = 100, message = "パスワードは8文字以上100文字以内\nで入力してください")
    private String password;

    public SignupForm() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
