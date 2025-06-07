package com.eduwise.repository;

import com.eduwise.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseIdOrderByOrderInCourseAsc(Long courseId);
    void deleteByCourseId(Long courseId);
    long countByCourseInstructorUsername(String username);
} 