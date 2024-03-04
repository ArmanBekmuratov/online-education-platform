package com.ab.eduplatform.entity;

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

import static org.assertj.core.api.Assertions.assertThat;

 class ProgressIT {

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    static void openSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSessionAndTransaction() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void closeSessionAndTransaction() {
        if (session.getTransaction() != null) {
            session.getTransaction().commit();
        }
    }

    @AfterAll
    static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    void checkProgressMappingRelationships() {
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
        Progress progress = Progress.builder()
                .user(user)
                .course(course)
                .completedLessons("1,2,3")
                .grade(100)
                .build();

        session.persist(user);
        session.persist(course);
        session.persist(progress);

        assertThat(progress.getId()).isNotNull();
        assertThat(progress.getUser()).isEqualTo(user);
        assertThat(progress.getCourse()).isEqualTo(course);
        assertThat(progress.getCompletedLessons()).isEqualTo("1,2,3");
        assertThat(progress.getGrade()).isEqualTo(100);
    }
}
