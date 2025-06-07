package com.eduwise.dto.quiz;

import lombok.Data;

@Data
public class AnswerResponse {
    private Long id;
    private String text;
    private boolean isCorrect;

    public AnswerResponse(Long id, String text, boolean isCorrect) {
        this.id = id;
        this.text = text;
        this.isCorrect = isCorrect;
    }
} 