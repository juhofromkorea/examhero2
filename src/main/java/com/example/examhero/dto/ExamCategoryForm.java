package com.example.examhero.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ExamCategoryForm {
    

    @NotBlank(message = "カテゴリ名は必須です")
    @Size(max = 50, message = "50文字以内で入力してください")
    private String name;  // getName() で取り出すやつ

    @Size(max = 200)
    private String description;

    public ExamCategoryForm() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
