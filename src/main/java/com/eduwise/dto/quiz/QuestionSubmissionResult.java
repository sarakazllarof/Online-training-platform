package com.eduwise.dto.quiz;

import lombok.Data;
import java.util.List;

@Data
public class QuestionSubmissionResult {
    private Long questionId;
    private String questionText;
    private Integer pointsEarned;
    private Integer totalPoints;
    private List<AnswerResult> answers;
    private List<Long> selectedAnswerIds;
    private boolean isCorrect;

    public QuestionSubmissionResult(Long questionId, String questionText, Integer pointsEarned, Integer totalPoints,
                                  List<AnswerResult> answers, List<Long> selectedAnswerIds, boolean isCorrect) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.pointsEarned = pointsEarned;
        this.totalPoints = totalPoints;
        this.answers = answers;
        this.selectedAnswerIds = selectedAnswerIds;
        this.isCorrect = isCorrect;
    }

    public QuestionSubmissionResult() {
    }
} 