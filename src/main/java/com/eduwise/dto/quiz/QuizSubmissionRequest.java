package com.eduwise.dto.quiz;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QuizSubmissionRequest {
    @NotNull
    private Long quizId;

    @Valid
    private List<QuestionSubmissionRequest> questionSubmissions;
} 