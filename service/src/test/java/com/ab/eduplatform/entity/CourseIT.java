package com.ab.eduplatform.entity;

import com.ab.eduplatform.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class CourseIT {

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
    void checkOneToManyCourseWithLessons() {
        Course course = getCourse();
        Lesson lessonStreamApi = Lesson.builder()
                .name("Stream Api")
                .description("stream API launched with java 8")
                .video("stream.mp4")
                .slides("slide 1")
                .test("What is stream?")
                .orderNumber(10)
                .build();
        Lesson lessonMultithreading = Lesson.builder()
                .name("Multithreading")
                .description("Multithreading is hard!!!")
                .video("multithreading.mp4")
                .slides("slide 1")
                .test("Is multithreading easy?")
                .orderNumber(11)
                .build();
        course.setLessons(Arrays.asList(lessonMultithreading, lessonStreamApi));

        session.persist(course);
        session.persist(lessonMultithreading);
        session.persist(lessonStreamApi);
        Course savedCourse = session.get(Course.class, course.getId());
        Lesson retrievedLessonMultithreading = session.get(Lesson.class, lessonMultithreading.getId());
        Lesson retrievedLessonStreamApi = session.get(Lesson.class, lessonStreamApi.getId());

        assertThat(savedCourse.getId()).isNotNull();
        assertThat(savedCourse.getLessons()).hasSize(2);
        assertThat(savedCourse.getLessons())
                .extracting(Lesson::getName)
                .contains("Stream Api");
        assertThat(savedCourse.getLessons())
                .extracting(Lesson::getName)
                .contains("Multithreading");
        assertThat(retrievedLessonStreamApi).isNotNull();
        assertThat(retrievedLessonMultithreading).isNotNull();
        assertThat(retrievedLessonMultithreading.getName()).isEqualTo("Multithreading");
        assertThat(retrievedLessonStreamApi.getName()).isEqualTo("Stream Api");
    }

    private Course getCourse() {
        return Course.builder()
                .name("java")
                .description("just java")
                .price(12.99)
                .level(Level.INTERMEDIATE)
                .category("programming")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .image("java.jpg")
                .build();
    }
}
