package com.ab.eduplatform.entity;

import com.ab.eduplatform.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LessonCompletionIT {

    private Session session;
    private Transaction transaction;

    @BeforeEach
    void openSession() {
        session = HibernateTestUtil.buildSessionFactory().openSession();
        transaction = session.beginTransaction();
    }

    @Test
    void checkManyToOneRelationships() {
        Progress progress = Progress.builder()
                .completedLessons("25")
                .grade(100)
                .build();
        Lesson lesson = Lesson.builder()
                .name("first")
                .description("some info")
                .video("video.mp4")
                .slides("first")
                .test("test")
                .orderNumber(100)
                .build();
        LessonCompletion lessonCompletion = LessonCompletion.builder()
                .progress(progress)
                .lesson(lesson)
                .build();

        session.persist(progress);
        session.persist(lesson);
        session.persist(lessonCompletion);
        LessonCompletion retrievedLessonCompletion = session.get(LessonCompletion.class, lessonCompletion.getId());

        assertThat(lessonCompletion.getId()).isNotNull();
        assertThat(lessonCompletion.getLesson()).isEqualTo(lesson);
        assertThat(lessonCompletion.getProgress()).isEqualTo(progress);
        assertThat(retrievedLessonCompletion.getId()).isNotNull();
        assertThat(retrievedLessonCompletion.getLesson()).isEqualTo(lesson);
        assertThat(retrievedLessonCompletion.getProgress()).isEqualTo(progress);
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
}
