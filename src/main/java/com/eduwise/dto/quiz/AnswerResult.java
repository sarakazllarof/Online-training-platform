package com.eduwise.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResult {
    private Long id;
    private String text;
    private boolean isCorrect;
} 