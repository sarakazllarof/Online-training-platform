package com.eduwise.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonResponse {
    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private String pdfUrl;
    private Integer orderInCourse;
} 