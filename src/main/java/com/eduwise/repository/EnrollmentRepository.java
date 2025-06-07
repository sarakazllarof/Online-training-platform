package com.eduwise.repository;

import com.eduwise.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourseId(Long courseId);
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    long countByCompletedTrue();
    long countByCompletedFalse();
    long countByProgress(double progress);
    long countByCourseInstructorUsername(String username);
    long countByCourseInstructorUsernameAndCompletedTrue(String username);
    long countByCourseInstructorUsernameAndCompletedFalse(String username);
    long countByCourseInstructorUsernameAndProgress(String username, double progress);
} 