package com.eduwise.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LessonRequest {
    @NotBlank
    private String title;

    private String description;

    private String videoUrl;

    private String pdfUrl;

    @NotNull
    private Integer orderInCourse;
} 