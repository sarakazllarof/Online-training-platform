package com.eduwise.controller;

import com.eduwise.dto.certificate.CertificateRequest;
import com.eduwise.dto.certificate.CertificateResponse;
import com.eduwise.service.CertificateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<CertificateResponse> generateCertificate(
            @Valid @RequestBody CertificateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(certificateService.generateCertificate(request));
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CertificateResponse>> getStudentCertificates(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(certificateService.getStudentCertificates(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateResponse> getCertificateById(@PathVariable Long id) {
        return ResponseEntity.ok(certificateService.getCertificateById(id));
    }

    @GetMapping("/number/{certificateNumber}")
    public ResponseEntity<CertificateResponse> getCertificateByNumber(
            @PathVariable String certificateNumber) {
        return ResponseEntity.ok(certificateService.getCertificateByNumber(certificateNumber));
    }
} 