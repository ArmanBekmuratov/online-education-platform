package com.ab.eduplatform.dao.repository;

import com.ab.eduplatform.entity.Course;
import com.ab.eduplatform.entity.Lesson;
import com.ab.eduplatform.entity.Level;
import com.ab.eduplatform.entity.User;
import com.ab.eduplatform.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CourseRepositoryIT {

    private static SessionFactory sessionFactory;
    private static Session session;
    private CourseRepository courseRepository;

    @BeforeAll
    static void openSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSessionAndTransaction() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        courseRepository = new CourseRepository(Course.class, session);
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
    void shouldCreateCourse() {
        Course course = getCourse();

        courseRepository.save(course);

        assertThat(course.getId()).isNotNull();
    }

    @Test
    void shouldGetCourse() {
        Course course = getCourse();
        courseRepository.save(course);

        Optional<Course> retrievedCourse = courseRepository.findById(course.getId());

        assertThat(retrievedCourse).isPresent();
        assertThat(retrievedCourse.get().getName()).isEqualTo("java");
        assertThat(retrievedCourse.get().getPrice()).isEqualTo(12.99);
    }

    @Test
    void shouldUpdateCourse() {
        Course course = getCourse();
        courseRepository.save(course);
        course.setCategory("updated category");
        course.setName("updated name");

        courseRepository.update(course);
        Optional<Course> updatedCourse = courseRepository.findById(course.getId());

        assertThat(updatedCourse).isPresent();
        assertThat(updatedCourse.get().getCategory()).isEqualTo("updated category");
        assertThat(updatedCourse.get().getName()).isEqualTo("updated name");
    }

    @Test
    void shouldDeleteCourse() {
        Course course = getCourse();
        courseRepository.save(course);

        courseRepository.delete(course.getId());
        session.evict(course);

        Optional<Course> deletedCourse = courseRepository.findById(course.getId());
        assertThat(deletedCourse).isEmpty();
    }

    @Test
    void shouldFindAllCourses() {
        Course course1 = getCourse();
        Course course2 = getCourse();
        Course course3 = getCourse();
        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);

        List<Course> courses = courseRepository.findAll();

        assertThat(courses).hasSize(3);
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
