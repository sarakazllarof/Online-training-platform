package com.eduwise.dto.quiz;

import lombok.Data;
import java.util.List;

@Data
public class QuestionResponse {
    private Long id;
    private String text;
    private Integer points;
    private List<AnswerResponse> answers;

    public QuestionResponse(Long id, String text, Integer points, List<AnswerResponse> answers) {
        this.id = id;
        this.text = text;
        this.points = points;
        this.answers = answers;
    }
} 