package com.eduwise.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Long categoryId;
} 