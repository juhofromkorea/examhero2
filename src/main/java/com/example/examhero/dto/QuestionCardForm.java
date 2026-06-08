package com.example.examhero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class QuestionCardForm {
    
    @NotNull(message = "カテゴリを選択してください")
    private Long examCategoryId;

    @NotBlank(message = "問題文を入力してください")
    @Size(max = 3000, message = "問題文は3000文字以内で入力してください")
    private String questionText;

    @NotBlank(message = "選択肢Aを入力してください")
    @Size(max = 500, message = "選択肢Aは500文字以内で入力してください")
    private String optionA;

    @NotBlank(message = "選択肢Bを入力してください")
    @Size(max = 500, message = "選択肢Bは500文字以内で入力してください")
    private String optionB;

    @NotBlank(message = "選択肢Cを入力してください")
    @Size(max = 500, message = "選択肢Cは500文字以内で入力してください")
    private String optionC;

    @NotBlank(message = "選択肢Dを入力してください")
    @Size(max = 500, message = "選択肢Dは500文字以内で入力してください")
    private String optionD;

    @NotBlank(message = "正解を選択してください")
    @Pattern(regexp = "A|B|C|D", message = "正解はA、B、C、Dのいずれかを選択してください")
    private String correctAnswer;

    @Size(max = 3000, message = "解説は3000文字以内で入力してください")
    private String explanation;

    public QuestionCardForm() {
    }

    public Long getExamCategoryId() {
        return examCategoryId;
    }

    public void setExamCategoryId(Long examCategoryId) {
        this.examCategoryId = examCategoryId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
