package com.eduwise.repository;

import com.eduwise.model.Certificate;
import com.eduwise.model.Course;
import com.eduwise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByUserId(Long userId);
    Optional<Certificate> findByUserIdAndCourseId(Long userId, Long courseId);
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    List<Certificate> findByUserUsername(String username);
    Optional<Certificate> findByCertificateNumber(String certificateNumber);
    boolean existsByUserAndCourse(User user, Course course);
} 