package com.eduwise.dto.quiz;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRequest {
    @NotBlank
    private String text;

    @NotNull
    @Positive
    private Integer points;

    @Valid
    private List<AnswerRequest> answers;
} 