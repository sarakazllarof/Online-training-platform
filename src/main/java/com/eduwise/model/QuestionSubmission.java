package com.eduwise.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class QuestionSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_submission_id", nullable = false)
    private QuizSubmission quizSubmission;

    @ManyToMany
    @JoinTable(
        name = "question_submission_answers",
        joinColumns = @JoinColumn(name = "question_submission_id"),
        inverseJoinColumns = @JoinColumn(name = "answer_id")
    )
    private List<Answer> selectedAnswers = new ArrayList<>();

    @Column(nullable = false)
    private boolean correct;

    @Column(nullable = false)
    private int pointsEarned;
} 