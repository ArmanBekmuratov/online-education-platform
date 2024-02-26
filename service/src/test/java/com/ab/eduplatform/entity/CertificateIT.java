package com.ab.eduplatform.entity;

import com.ab.eduplatform.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CertificateIT {

    private static Session session;
    private Transaction transaction;

    @BeforeAll
    static void openSession() {
        session = HibernateTestUtil.buildSessionFactory().openSession();
    }

    @BeforeEach
    void openTransaction() {
        transaction = session.beginTransaction();
    }

    @AfterEach
    void closeTransaction() {
        if (transaction != null) {
            transaction.commit();
        }
    }

    @AfterAll
    static void closeSession() {
        if (session != null) {
            session.close();
        }
    }

    @Test
    void shouldCreateCertificate() {
        Certificate certificate = getCertificate();

        session.persist(certificate);

        assertThat(certificate.getId()).isNotNull();
    }

    @Test
    void shouldGetCertificate() {
        Certificate certificate = getCertificate();
        session.persist(certificate);

        Certificate retrievedCertificate = session.get(Certificate.class, certificate.getId());

        assertThat(retrievedCertificate).isNotNull();
        assertThat(retrievedCertificate.getGrade()).isEqualTo(100);
    }

    @Test
    void shouldUpdateCertificate() {
        Certificate certificate = getCertificate();
        session.persist(certificate);
        certificate.setGrade(0);
        certificate.setIssueDate(LocalDate.now());

        session.merge(certificate);
        Certificate updatedCertificate = session.get(Certificate.class, certificate.getId());

        assertThat(updatedCertificate).isNotNull();
        assertThat(certificate.getGrade()).isEqualTo(0);
    }

    @Test
    void shouldDeleteCertificate() {
        Long certificateId;
        Certificate certificate = getCertificate();
        session.persist(certificate);
        certificateId = certificate.getId();

        session.remove(certificate);
        Certificate deletedCertificate = session.get(Certificate.class, certificateId);

        assertThat(deletedCertificate).isNull();
    }

    @Test
    void checkManyToOneRelationships() {
        User user = User.builder()
                .firstname("John")
                .lastname("King")
                .email("johnking@gmail.com")
                .password("123")
                .role(Role.STUDENT)
                .registrationDate(Instant.now())
                .build();
        Course course = Course.builder()
                .name("java")
                .description("just java")
                .price(12.99)
                .level(Level.INTERMEDIATE)
                .category("programming")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .image("java.jpg")
                .build();
        Certificate certificate = Certificate.builder()
                .user(user)
                .course(course)
                .issueDate(LocalDate.now())
                .grade(100)
                .build();

        session.persist(user);
        session.persist(course);
        session.persist(certificate);
        Certificate retrievedCertificate = session.get(Certificate.class, certificate.getId());

        assertThat(certificate.getId()).isNotNull();
        assertThat(certificate.getUser()).isEqualTo(user);
        assertThat(certificate.getCourse()).isEqualTo(course);
        assertThat(retrievedCertificate).isNotNull();
        assertThat(retrievedCertificate.getUser()).isEqualTo(user);
        assertThat(retrievedCertificate.getCourse()).isEqualTo(course);
    }

    private Certificate getCertificate() {
        return Certificate.builder()
                .issueDate(LocalDate.now())
                .grade(100)
                .build();
    }
}
