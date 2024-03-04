package com.ab.eduplatform.dao.repository;

import com.ab.eduplatform.entity.Certificate;
import com.ab.eduplatform.entity.Course;
import com.ab.eduplatform.entity.Level;
import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.entity.User;
import com.ab.eduplatform.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CertificateRepositoryIT {

    private static SessionFactory sessionFactory;
    private static Session session;
    private CertificateRepository certificateRepository;

    @BeforeAll
    static void openSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSessionAndTransaction() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        certificateRepository = new CertificateRepository(Certificate.class, session);
    }

    @AfterEach
    void closeSessionAndTransaction() {
        if (session.getTransaction().isActive()) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    @AfterAll
    static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

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

        certificateRepository.delete(certificate.getId());
        session.evict(certificate);

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
