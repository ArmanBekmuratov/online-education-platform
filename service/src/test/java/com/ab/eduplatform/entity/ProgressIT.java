package com.ab.eduplatform.entity;

import com.ab.eduplatform.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ProgressIT {

    private Session session;
    private Transaction transaction;

    @BeforeEach
    void openSession() {
        session = HibernateTestUtil.buildSessionFactory().openSession();
        transaction = session.beginTransaction();
    }

    @Test
    void shouldCreateProgress() {
        Progress progress = getProgress();

        session.persist(progress);

        assertThat(progress.getId()).isNotNull();
    }

    @Test
    void shouldGetProgress() {
        Progress progress = getProgress();
        session.persist(progress);

        Progress retrievedProgress = session.get(Progress.class, progress.getId());

        assertThat(retrievedProgress).isNotNull();
        assertThat(retrievedProgress.getGrade()).isEqualTo(100);
        assertThat(retrievedProgress.getCompletedLessons()).isEqualTo("25");
    }

    @Test
    void shouldUpdateProgress() {
        Progress progress = getProgress();
        session.persist(progress);
        progress.setGrade(0);
        progress.setCompletedLessons("updated number");

        session.merge(progress);
        Progress updatedProgress = session.get(Progress.class, progress.getId());

        assertThat(updatedProgress).isNotNull();
        assertThat(updatedProgress.getGrade()).isEqualTo(0);
        assertThat(updatedProgress.getCompletedLessons()).isEqualTo("updated number");
    }

    @Test
    void shouldDeleteProgress() {
        Long progressId;
        Progress progress = getProgress();
        session.persist(progress);
        progressId = progress.getId();

        session.remove(progress);
        Progress deletedProgress = session.get(Progress.class, progressId);

        assertThat(deletedProgress).isNull();
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

    @AfterEach
    void closeSession() {
        if (transaction != null) {
            transaction.commit();
        }

        if (session != null) {
            session.close();
        }
    }

    private Progress getProgress() {
        return Progress.builder()
                .completedLessons("25")
                .grade(100)
                .build();
    }
}
