package com.eduwise.service;

import com.eduwise.dto.lesson.LessonRequest;
import com.eduwise.dto.lesson.LessonResponse;
import com.eduwise.model.Course;
import com.eduwise.model.Lesson;
import com.eduwise.repository.CourseRepository;
import com.eduwise.repository.LessonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public LessonResponse createLesson(Long courseId, LessonRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setDescription(request.getDescription());
        lesson.setVideoUrl(request.getVideoUrl());
        lesson.setPdfUrl(request.getPdfUrl());
        lesson.setOrderInCourse(request.getOrderInCourse());
        lesson.setCourse(course);

        return mapToLessonResponse(lessonRepository.save(lesson));
    }

    @Transactional
    public LessonResponse updateLesson(Long id, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        lesson.setTitle(request.getTitle());
        lesson.setDescription(request.getDescription());
        lesson.setVideoUrl(request.getVideoUrl());
        lesson.setPdfUrl(request.getPdfUrl());
        lesson.setOrderInCourse(request.getOrderInCourse());

        return mapToLessonResponse(lessonRepository.save(lesson));
    }

    @Transactional
    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new EntityNotFoundException("Lesson not found");
        }
        lessonRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseIdOrderByOrderInCourseAsc(courseId).stream()
                .map(this::mapToLessonResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LessonResponse getLessonById(Long id) {
        return lessonRepository.findById(id)
                .map(this::mapToLessonResponse)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
    }

    private LessonResponse mapToLessonResponse(Lesson lesson) {
        return new LessonResponse(
            lesson.getId(),
            lesson.getTitle(),
            lesson.getDescription(),
            lesson.getVideoUrl(),
            lesson.getPdfUrl(),
            lesson.getOrderInCourse()
        );
    }
} 