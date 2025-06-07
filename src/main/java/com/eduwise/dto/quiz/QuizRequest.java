package com.eduwise.dto.quiz;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class QuizRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    @Positive
    private Integer timeLimit;

    @NotNull
    @Positive
    private Integer passingScore;

    @Valid
    private List<QuestionRequest> questions;
} 