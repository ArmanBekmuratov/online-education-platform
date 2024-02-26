package com.ab.eduplatform.entity;

import com.ab.eduplatform.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LessonIT {

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
    void shouldCreateLesson() {
        Lesson lesson = getLesson();

        session.persist(lesson);

        assertThat(lesson.getId()).isNotNull();
    }

    @Test
    void shouldGetLesson() {
        Lesson lesson = getLesson();
        session.persist(lesson);

        Lesson retrievedLesson = session.get(Lesson.class, lesson.getId());

        assertThat(retrievedLesson).isNotNull();
        assertThat(retrievedLesson.getName()).isEqualTo("first");
    }

    @Test
    void shouldUpdateLesson() {
        Lesson lesson = getLesson();
        session.persist(lesson);
        lesson.setDescription("updated description");
        lesson.setName("updated name");
        lesson.setOrderNumber(10000);

        session.merge(lesson);
        Lesson updatedLesson = session.get(Lesson.class, lesson.getId());

        assertThat(updatedLesson).isNotNull();
        assertThat(lesson.getOrderNumber()).isEqualTo(10000);
        assertThat(lesson.getDescription()).isEqualTo("updated description");
        assertThat(lesson.getName()).isEqualTo("updated name");
    }

    @Test
    void shouldDeleteLesson() {
        Long lessonId;
        Lesson lesson = getLesson();
        session.persist(lesson);
        lessonId = lesson.getId();

        session.remove(lesson);
        Lesson deletedLesson = session.get(Lesson.class, lessonId);

        assertThat(deletedLesson).isNull();
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
