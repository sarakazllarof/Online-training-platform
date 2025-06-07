package com.eduwise.dto.certificate;

import com.eduwise.dto.course.CourseResponse;
import com.eduwise.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateResponse {
    private Long id;
    private String certificateNumber;
    private UserResponse user;
    private CourseResponse course;
    private LocalDateTime issueDate;
    private String certificateUrl;
} 