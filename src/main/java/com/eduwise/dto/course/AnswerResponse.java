package com.eduwise.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponse {
    private Long id;
    private String text;
    private boolean isCorrect;
} 