package com.eduwise.dto.quiz;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QuestionSubmissionRequest {
    @NotNull
    private Long questionId;
    
    @NotNull
    private List<Long> selectedAnswerIds;
} 