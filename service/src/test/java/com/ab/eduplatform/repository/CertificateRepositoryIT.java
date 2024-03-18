package com.ab.eduplatform.repository;

import com.ab.eduplatform.entity.Certificate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class CertificateRepositoryIT extends IntegrationTestBase{

    private final CertificateRepository certificateRepository;

    @Test
    void shouldCreateCertificate() {
        Certificate certificate = getCertificate();

        certificateRepository.save(certificate);

        assertThat(certificate.getId()).isNotNull();
    }

    @Test
    void shouldGetCertificate() {
        Certificate certificate = getCertificate();
        certificateRepository.save(certificate);

        Optional<Certificate> retrievedCertificate = certificateRepository.findById(certificate.getId());

        assertThat(retrievedCertificate).isPresent();
    }

    @Test
    void shouldUpdateCertificate() {
        Certificate certificate = getCertificate();
        certificateRepository.save(certificate);
        certificate.setGrade(0);
        certificate.setIssueDate(LocalDate.now());

        certificateRepository.update(certificate);
        Optional<Certificate> updatedCertificate = certificateRepository.findById(certificate.getId());

        assertThat(updatedCertificate).isPresent();
        assertThat(certificate.getGrade()).isEqualTo(0);
    }

    @Test
    void shouldDeleteCertificate() {
        Certificate certificate = getCertificate();
        certificateRepository.save(certificate);

        certificateRepository.delete(certificate);
        entityManager.clear();

        Optional<Certificate> deletedCertificate = certificateRepository.findById(certificate.getId());
        assertThat(deletedCertificate).isEmpty();
    }

    @Test
    void shouldFindAllCertificates() {
        Certificate certificate1 = getCertificate();
        Certificate certificate2 = getCertificate();
        Certificate certificate3 = getCertificate();
        certificateRepository.save(certificate1);
        certificateRepository.save(certificate2);
        certificateRepository.save(certificate3);

        List<Certificate> certificates = certificateRepository.findAll();

        assertThat(certificates).hasSize(3);
    }

    private Certificate getCertificate() {
        return Certificate.builder()
                .issueDate(LocalDate.now())
                .grade(100)
                .build();
    }
}
