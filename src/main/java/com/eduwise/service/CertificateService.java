package com.eduwise.service;

import com.eduwise.dto.certificate.CertificateRequest;
import com.eduwise.dto.certificate.CertificateResponse;
import com.eduwise.dto.user.UserResponse;
import com.eduwise.model.Certificate;
import com.eduwise.model.Enrollment;
import com.eduwise.model.User;
import com.eduwise.repository.CertificateRepository;
import com.eduwise.repository.EnrollmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseService courseService;

    @Transactional
    public CertificateResponse generateCertificate(CertificateRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));

        if (!enrollment.isCompleted()) {
            throw new IllegalStateException("Course must be completed before generating a certificate");
        }

        if (certificateRepository.existsByUserAndCourse(enrollment.getStudent(), enrollment.getCourse())) {
            throw new IllegalStateException("Certificate already exists for this enrollment");
        }

        Certificate certificate = new Certificate();
        certificate.setCertificateNumber(generateCertificateNumber());
        certificate.setUser(enrollment.getStudent());
        certificate.setCourse(enrollment.getCourse());
        certificate.setIssueDate(LocalDateTime.now());
        certificate.setCertificateUrl(generateCertificateUrl(certificate.getCertificateNumber()));

        return mapToCertificateResponse(certificateRepository.save(certificate));
    }

    @Transactional(readOnly = true)
    public List<CertificateResponse> getStudentCertificates(String studentUsername) {
        return certificateRepository.findByUserUsername(studentUsername).stream()
                .map(this::mapToCertificateResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CertificateResponse getCertificateById(Long id) {
        return certificateRepository.findById(id)
                .map(this::mapToCertificateResponse)
                .orElseThrow(() -> new EntityNotFoundException("Certificate not found"));
    }

    @Transactional(readOnly = true)
    public CertificateResponse getCertificateByNumber(String certificateNumber) {
        return certificateRepository.findByCertificateNumber(certificateNumber)
                .map(this::mapToCertificateResponse)
                .orElseThrow(() -> new EntityNotFoundException("Certificate not found"));
    }

    private String generateCertificateNumber() {
        return "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateCertificateUrl(String certificateNumber) {
        // In a real application, this would generate a URL to a stored certificate file
        return "/api/certificates/" + certificateNumber + "/download";
    }

    private CertificateResponse mapToCertificateResponse(Certificate certificate) {
        return new CertificateResponse(
            certificate.getId(),
            certificate.getCertificateNumber(),
            mapToUserResponse(certificate.getUser()),
            courseService.getCourseById(certificate.getCourse().getId()),
            certificate.getIssueDate(),
            certificate.getCertificateUrl()
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