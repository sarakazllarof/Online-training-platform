package com.eduwise.service;

import com.eduwise.dto.enrollment.EnrollmentResponse;
import com.eduwise.dto.user.UserResponse;
import com.eduwise.model.Course;
import com.eduwise.model.Enrollment;
import com.eduwise.model.User;
import com.eduwise.repository.CourseRepository;
import com.eduwise.repository.EnrollmentRepository;
import com.eduwise.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;

    @Transactional
    public EnrollmentResponse enrollInCourse(Long courseId, String studentUsername) {
        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new IllegalStateException("Student is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setProgress(0.0);
        enrollment.setCompleted(false);

        return mapToEnrollmentResponse(enrollmentRepository.save(enrollment));
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getStudentEnrollments(String studentUsername) {
        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        return enrollmentRepository.findByStudentId(student.getId()).stream()
                .map(this::mapToEnrollmentResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EnrollmentResponse getEnrollmentProgress(Long courseId, String studentUsername) {
        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        return enrollmentRepository.findByStudentIdAndCourseId(student.getId(), courseId)
                .map(this::mapToEnrollmentResponse)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
    }

    @Transactional
    public void updateProgress(Long enrollmentId, double progress) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));

        enrollment.setProgress(progress);
        if (progress >= 100.0 && !enrollment.isCompleted()) {
            enrollment.setCompleted(true);
            enrollment.setCompletionDate(LocalDateTime.now());
        }

        enrollmentRepository.save(enrollment);
    }

    private EnrollmentResponse mapToEnrollmentResponse(Enrollment enrollment) {
        return new EnrollmentResponse(
            enrollment.getId(),
            mapToUserResponse(enrollment.getStudent()),
            courseService.getCourseById(enrollment.getCourse().getId()),
            enrollment.getEnrollmentDate(),
            enrollment.getProgress(),
            enrollment.isCompleted(),
            enrollment.getCompletionDate()
        );
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
        );
    }
} 