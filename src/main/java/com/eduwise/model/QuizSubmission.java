package com.eduwise.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class QuizSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false)
    private LocalDateTime submissionDate;

    @OneToMany(mappedBy = "quizSubmission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionSubmission> questionSubmissions = new ArrayList<>();

    @Column(nullable = false)
    private int totalScore;

    @Column(nullable = false)
    private int maxScore;

    @Column(nullable = false)
    private boolean passed;
} 