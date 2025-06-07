package com.eduwise.dto.quiz;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnswerRequest {
    @NotBlank
    private String text;
    
    private boolean isCorrect;
} 