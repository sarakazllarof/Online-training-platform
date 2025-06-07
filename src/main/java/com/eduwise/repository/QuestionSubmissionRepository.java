package com.eduwise.repository;

import com.eduwise.model.QuestionSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionSubmissionRepository extends JpaRepository<QuestionSubmission, Long> {
} 