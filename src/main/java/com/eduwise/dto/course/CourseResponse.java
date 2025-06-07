package com.eduwise.dto.course;

import com.eduwise.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private UserResponse instructor;
    private CategoryResponse category;
    private boolean published;
    private List<LessonResponse> lessons;
    private List<QuizResponse> quizzes;
} 