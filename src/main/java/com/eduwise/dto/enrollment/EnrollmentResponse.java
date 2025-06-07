package com.eduwise.dto.enrollment;

import com.eduwise.dto.course.CourseResponse;
import com.eduwise.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {
    private Long id;
    private UserResponse student;
    private CourseResponse course;
    private LocalDateTime enrollmentDate;
    private double progress;
    private boolean completed;
    private LocalDateTime completionDate;
} 