package com.eduwise.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    private long totalStudents;
    private long totalInstructors;
    private long totalCourses;
    private long totalEnrollments;
    private long totalCompletedCourses;
    private long totalCertificates;
    private double averageCourseRating;
    private double averageCompletionRate;
    private long totalQuizzes;
    private long totalLessons;
    private long activeEnrollments;
    private long pendingEnrollments;
} 