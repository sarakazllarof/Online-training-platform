package com.eduwise.service;

import com.eduwise.dto.dashboard.DashboardStatsResponse;
import com.eduwise.model.Role;
import com.eduwise.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CertificateRepository certificateRepository;
    private final QuizRepository quizRepository;
    private final LessonRepository lessonRepository;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        long totalStudents = userRepository.countByRole(Role.STUDENT);
        long totalInstructors = userRepository.countByRole(Role.INSTRUCTOR);
        long totalCourses = courseRepository.count();
        long totalEnrollments = enrollmentRepository.count();
        long totalCompletedCourses = enrollmentRepository.countByCompletedTrue();
        long totalCertificates = certificateRepository.count();
        long totalQuizzes = quizRepository.count();
        long totalLessons = lessonRepository.count();
        long activeEnrollments = enrollmentRepository.countByCompletedFalse();
        long pendingEnrollments = enrollmentRepository.countByProgress(0.0);

        double averageCourseRating = courseRepository.findAll().stream()
                .mapToDouble(course -> course.getRatings().stream()
                        .mapToDouble(rating -> rating.getValue())
                        .average()
                        .orElse(0.0))
                .average()
                .orElse(0.0);

        double averageCompletionRate = totalEnrollments > 0
                ? (double) totalCompletedCourses / totalEnrollments * 100
                : 0.0;

        return new DashboardStatsResponse(
            totalStudents,
            totalInstructors,
            totalCourses,
            totalEnrollments,
            totalCompletedCourses,
            totalCertificates,
            averageCourseRating,
            averageCompletionRate,
            totalQuizzes,
            totalLessons,
            activeEnrollments,
            pendingEnrollments
        );
    }

    @Transactional(readOnly = true)
    public DashboardStatsResponse getInstructorStats(String instructorUsername) {
        long totalCourses = courseRepository.countByInstructorUsername(instructorUsername);
        long totalEnrollments = enrollmentRepository.countByCourseInstructorUsername(instructorUsername);
        long totalCompletedCourses = enrollmentRepository.countByCourseInstructorUsernameAndCompletedTrue(instructorUsername);
        long totalQuizzes = quizRepository.countByCourseInstructorUsername(instructorUsername);
        long totalLessons = lessonRepository.countByCourseInstructorUsername(instructorUsername);
        long activeEnrollments = enrollmentRepository.countByCourseInstructorUsernameAndCompletedFalse(instructorUsername);
        long pendingEnrollments = enrollmentRepository.countByCourseInstructorUsernameAndProgress(instructorUsername, 0.0);

        double averageCourseRating = courseRepository.findByInstructorUsername(instructorUsername).stream()
                .mapToDouble(course -> course.getRatings().stream()
                        .mapToDouble(rating -> rating.getValue())
                        .average()
                        .orElse(0.0))
                .average()
                .orElse(0.0);

        double averageCompletionRate = totalEnrollments > 0
                ? (double) totalCompletedCourses / totalEnrollments * 100
                : 0.0;

        return new DashboardStatsResponse(
            0, // Not applicable for instructor stats
            0, // Not applicable for instructor stats
            totalCourses,
            totalEnrollments,
            totalCompletedCourses,
            0, // Not applicable for instructor stats
            averageCourseRating,
            averageCompletionRate,
            totalQuizzes,
            totalLessons,
            activeEnrollments,
            pendingEnrollments
        );
    }
} 