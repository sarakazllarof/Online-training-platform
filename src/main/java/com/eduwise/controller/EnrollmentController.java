package com.eduwise.controller;

import com.eduwise.dto.enrollment.EnrollmentResponse;
import com.eduwise.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentResponse> enrollInCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(enrollmentService.enrollInCourse(courseId, userDetails.getUsername()));
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EnrollmentResponse>> getStudentEnrollments(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(enrollmentService.getStudentEnrollments(userDetails.getUsername()));
    }

    @GetMapping("/courses/{courseId}/progress")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentResponse> getEnrollmentProgress(
            @PathVariable Long courseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentProgress(courseId, userDetails.getUsername()));
    }

    @PutMapping("/{enrollmentId}/progress")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> updateProgress(
            @PathVariable Long enrollmentId,
            @RequestParam double progress) {
        enrollmentService.updateProgress(enrollmentId, progress);
        return ResponseEntity.ok().build();
    }
} 