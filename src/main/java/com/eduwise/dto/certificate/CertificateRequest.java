package com.eduwise.dto.certificate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CertificateRequest {
    @NotNull
    private Long enrollmentId;
    
    private String additionalNotes;
} 