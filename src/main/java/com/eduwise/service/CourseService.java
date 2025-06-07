package com.eduwise.service;

import com.eduwise.dto.course.*;
import com.eduwise.dto.user.UserResponse;
import com.eduwise.model.*;
import com.eduwise.repository.CategoryRepository;
import com.eduwise.repository.CourseRepository;
import com.eduwise.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public CourseResponse createCourse(CourseRequest request, String instructorUsername) {
        User instructor = userRepository.findByUsername(instructorUsername)
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found"));
        
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setInstructor(instructor);
        course.setCategory(category);
        course.setPublished(false);

        return mapToCourseResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(category);

        return mapToCourseResponse(courseRepository.save(course));
    }

    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Course not found");
        }
        courseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(this::mapToCourseResponse)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByCategory(Long categoryId) {
        return courseRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> searchCourses(String searchTerm) {
        return courseRepository.searchCourses(searchTerm).stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    private CourseResponse mapToCourseResponse(Course course) {
        return new CourseResponse(
            course.getId(),
            course.getTitle(),
            course.getDescription(),
            mapToUserResponse(course.getInstructor()),
            mapToCategoryResponse(course.getCategory()),
            course.isPublished(),
            course.getLessons().stream()
                .map(this::mapToLessonResponse)
                .collect(Collectors.toList()),
            course.getQuizzes().stream()
                .map(this::mapToQuizResponse)
                .collect(Collectors.toList())
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

    private CategoryResponse mapToCategoryResponse(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getDescription()
        );
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

    private QuizResponse mapToQuizResponse(Quiz quiz) {
        return new QuizResponse(
            quiz.getId(),
            quiz.getTitle(),
            quiz.getDescription(),
            quiz.getTimeLimit(),
            quiz.getPassingScore(),
            quiz.getQuestions().stream()
                .map(this::mapToQuestionResponse)
                .collect(Collectors.toList())
        );
    }

    private QuestionResponse mapToQuestionResponse(Question question) {
        return new QuestionResponse(
            question.getId(),
            question.getText(),
            question.getPoints(),
            question.getAnswers().stream()
                .map(this::mapToAnswerResponse)
                .collect(Collectors.toList())
        );
    }

    private AnswerResponse mapToAnswerResponse(Answer answer) {
        return new AnswerResponse(
            answer.getId(),
            answer.getText(),
            answer.isCorrect()
        );
    }
} 