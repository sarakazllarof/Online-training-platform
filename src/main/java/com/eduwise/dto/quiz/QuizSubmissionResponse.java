package com.eduwise.dto.quiz;

import com.eduwise.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmissionResponse {
    private Long id;
    private UserResponse student;
    private QuizResponse quiz;
    private LocalDateTime submissionDate;
    private int totalScore;
    private int maxScore;
    private boolean passed;
    private List<QuestionSubmissionResult> questionResults;
} 