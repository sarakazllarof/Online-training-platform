package com.eduwise.repository;

import com.eduwise.model.QuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    List<QuizSubmission> findByStudentUsername(String username);
    boolean existsByStudentIdAndQuizId(Long studentId, Long quizId);
} 