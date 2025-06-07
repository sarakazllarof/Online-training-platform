package com.eduwise.controller;

import com.eduwise.dto.quiz.QuizSubmissionRequest;
import com.eduwise.dto.quiz.QuizSubmissionResponse;
import com.eduwise.service.QuizSubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-submissions")
@RequiredArgsConstructor
public class QuizSubmissionController {

    private final QuizSubmissionService quizSubmissionService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<QuizSubmissionResponse> submitQuiz(
            @Valid @RequestBody QuizSubmissionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(quizSubmissionService.submitQuiz(request, userDetails.getUsername()));
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<QuizSubmissionResponse>> getStudentSubmissions(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(quizSubmissionService.getStudentSubmissions(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR', 'ADMIN')")
    public ResponseEntity<QuizSubmissionResponse> getSubmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(quizSubmissionService.getSubmissionById(id));
    }
} 