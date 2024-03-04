package com.ab.eduplatform.dao;

import com.ab.eduplatform.dto.CategoryFilter;
import com.ab.eduplatform.entity.Course;
import com.ab.eduplatform.entity.Level;
import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.entity.User;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CourseDaoIT {

    private static SessionFactory sessionFactory;
    private static Session session;
    private CourseDao courseDao;

    @BeforeAll
    static void openSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSessionAndTransaction() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        courseDao = CourseDao.getInstance();
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
    void findCoursesByCategoryAndLevel() {
        CategoryFilter filter = CategoryFilter.builder()
                .category("programming")
                .level(Level.INTERMEDIATE)
                .build();
        Course expectedCourse = getCourse();
        session.persist(expectedCourse);

        List<Course> courses = courseDao.findCoursesByCategoryAndLevel(session, filter);

        assertThat(courses).isNotEmpty();
        assertThat(courses.get(0).getName()).isEqualTo(expectedCourse.getName());
    }

    @Test
    void findCoursesByTeacherName() {
        User teacher = getUser();
        session.persist(teacher);
        Course expectedCourse = getCourse();
        expectedCourse.setTeacher(teacher);
        session.persist(expectedCourse);

        List<Course> courses = courseDao.findCoursesByTeacherName(session, "John King");

        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getName()).isEqualTo(expectedCourse.getName());
    }

    @Test
    void findCoursesByPriceRange() {
        Course course = getCourse();
        session.persist(course);

        List<Course> coursesByPriceRange = courseDao.findCoursesByPriceRange(session, 12.0, 13.0);

        assertThat(coursesByPriceRange).hasSize(1);
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

    private User getUser() {
        return User.builder()
                .firstname("John")
                .lastname("King")
                .email("johnking@gmail.com")
                .password("123")
                .role(Role.TEACHER)
                .registrationDate(Instant.now())
                .build();
    }
}