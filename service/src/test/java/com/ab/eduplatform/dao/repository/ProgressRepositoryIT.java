package com.ab.eduplatform.dao.repository;

import com.ab.eduplatform.entity.Progress;
import com.ab.eduplatform.entity.User;
import com.ab.eduplatform.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProgressRepositoryIT {

    private static SessionFactory sessionFactory;
    private static Session session;
    private ProgressRepository progressRepository;

    @BeforeAll
    static void openSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSessionAndTransaction() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        progressRepository = new ProgressRepository(Progress.class, session);
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
    void shouldCreateProgress() {
        Progress progress = getProgress();

        progressRepository.save(progress);

        assertThat(progress.getId()).isNotNull();
    }

    @Test
    void shouldGetProgress() {
        Progress progress = getProgress();
        progressRepository.save(progress);

        Optional<Progress> retrievedProgress = progressRepository.findById(progress.getId());

        assertThat(retrievedProgress).isPresent();
        assertThat(retrievedProgress.get().getGrade()).isEqualTo(100);
        assertThat(retrievedProgress.get().getCompletedLessons()).isEqualTo("25");
    }

    @Test
    void shouldUpdateProgress() {
        Progress progress = getProgress();
        progressRepository.save(progress);
        progress.setGrade(0);
        progress.setCompletedLessons("updated number");

        progressRepository.update(progress);
        Optional<Progress> updatedProgress = progressRepository.findById(progress.getId());

        assertThat(updatedProgress).isPresent();
        assertThat(updatedProgress.get().getGrade()).isEqualTo(0);
        assertThat(updatedProgress.get().getCompletedLessons()).isEqualTo("updated number");
    }

    @Test
    void shouldDeleteProgress() {
        Progress progress = getProgress();
        progressRepository.save(progress);

        progressRepository.delete(progress.getId());
        session.evict(progress);

        Optional<Progress> deletedProgress = progressRepository.findById(progress.getId());
        assertThat(deletedProgress).isEmpty();
    }

    @Test
    void shouldFindAllProgresses() {
        Progress progress1 = getProgress();
        Progress progress2 = getProgress();
        Progress progress3 = getProgress();
        progressRepository.save(progress1);
        progressRepository.save(progress2);
        progressRepository.save(progress3);

        List<Progress> progresses = progressRepository.findAll();

        assertThat(progresses).hasSize(3);
    }

    private Progress getProgress() {
        return Progress.builder()
                .completedLessons("25")
                .grade(100)
                .build();
    }
}
