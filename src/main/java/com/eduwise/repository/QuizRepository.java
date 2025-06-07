package com.eduwise.repository;

import com.eduwise.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCourseId(Long courseId);
    void deleteByCourseId(Long courseId);
    long countByCourseInstructorUsername(String username);
} 