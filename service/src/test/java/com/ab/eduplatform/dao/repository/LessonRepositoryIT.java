package com.ab.eduplatform.dao.repository;

import com.ab.eduplatform.entity.Lesson;
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

class LessonRepositoryIT {

    private static SessionFactory sessionFactory;
    private static Session session;
    private LessonRepository lessonRepository;

    @BeforeAll
    static void openSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSessionAndTransaction() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        lessonRepository = new LessonRepository(Lesson.class, session);
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
    void shouldCreateLesson() {
        Lesson lesson = getLesson();

        lessonRepository.save(lesson);

        assertThat(lesson.getId()).isNotNull();
    }

    @Test
    void shouldGetLesson() {
        Lesson lesson = getLesson();
        lessonRepository.save(lesson);

        Optional<Lesson> retrievedLesson = lessonRepository.findById(lesson.getId());

        assertThat(retrievedLesson).isPresent();
    }

    @Test
    void shouldUpdateLesson() {
        Lesson lesson = getLesson();
        lessonRepository.save(lesson);
        lesson.setDescription("updated description");
        lesson.setName("updated name");
        lesson.setOrderNumber(10000);

        lessonRepository.update(lesson);
        Optional<Lesson> updatedLesson = lessonRepository.findById(lesson.getId());

        assertThat(updatedLesson).isPresent();
        assertThat(lesson.getOrderNumber()).isEqualTo(10000);
        assertThat(lesson.getDescription()).isEqualTo("updated description");
        assertThat(lesson.getName()).isEqualTo("updated name");
    }

    @Test
    void shouldDeleteLesson() {
        Lesson lesson = getLesson();
        lessonRepository.save(lesson);

        lessonRepository.delete(lesson.getId());
        session.evict(lesson);

        Optional<Lesson> deletedLesson = lessonRepository.findById(lesson.getId());
        assertThat(deletedLesson).isEmpty();
    }

    @Test
    void shouldFindAllLessons() {
        Lesson lesson1 = getLesson();
        Lesson lesson2 = getLesson();
        Lesson lesson3 = getLesson();
        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);
        lessonRepository.save(lesson3);


        List<Lesson> lessons = lessonRepository.findAll();

        assertThat(lessons).hasSize(3);
    }

    private Lesson getLesson() {
        return Lesson.builder()
                .name("first")
                .description("some info")
                .video("video.mp4")
                .slides("first")
                .test("test")
                .orderNumber(100)
                .build();
    }
}