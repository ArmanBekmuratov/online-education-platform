package com.ab.eduplatform.entity;

import com.ab.eduplatform.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class CourseIT {

    private Session session;
    private Transaction transaction;

    @BeforeEach
    void openSession() {
        session = HibernateTestUtil.buildSessionFactory().openSession();
        transaction = session.beginTransaction();
    }

    @Test
    void shouldCreateCourse() {
        Course course = getCourse();

        session.persist(course);

        assertThat(course.getId()).isNotNull();
        Course savedCourse = session.get(Course.class, course.getId());
        assertThat(savedCourse).isNotNull();
        assertThat(savedCourse.getName()).isEqualTo("java");
        assertThat(savedCourse.getDescription()).isEqualTo("just java");
        assertThat(savedCourse.getPrice()).isEqualTo(12.99);
        assertThat(savedCourse.getLevel()).isEqualTo(Level.INTERMEDIATE);
        assertThat(savedCourse.getCategory()).isEqualTo("programming");
        assertThat(savedCourse.getStartDate()).isEqualTo(LocalDate.now()    );
        assertThat(savedCourse.getEndDate()).isEqualTo(LocalDate.now().plusMonths(1));
        assertThat(savedCourse.getImage()).isEqualTo("java.jpg");
    }

    @Test
    void shouldGetCourse() {
        Course course = getCourse();
        session.persist(course);

        Course retrievedCourse = session.get(Course.class, course.getId());

        assertThat(retrievedCourse).isNotNull();
        assertThat(retrievedCourse.getName()).isEqualTo("java");
        assertThat(retrievedCourse.getPrice()).isEqualTo(12.99);
    }

    @Test
    void shouldUpdateCourse() {
        Course course = getCourse();
        session.persist(course);
        course.setCategory("updated category");
        course.setName("updated name");

        session.merge(course);
        Course updatedCourse = session.get(Course.class, course.getId());

        assertThat(updatedCourse).isNotNull();
        assertThat(updatedCourse.getCategory()).isEqualTo("updated category");
        assertThat(updatedCourse.getName()).isEqualTo("updated name");
    }

    @Test
    void shouldDeleteCourse() {
        Long courseId;
        Course course = getCourse();
        session.persist(course);
        courseId = course.getId();

        session.remove(course);
        Course deletedCourse = session.get(Course.class, courseId);

        assertThat(deletedCourse).isNull();
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

    @AfterEach
    void closeSession() {
        if (transaction != null) {
            transaction.commit();
        }

        if (session != null) {
            session.close();
        }
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
