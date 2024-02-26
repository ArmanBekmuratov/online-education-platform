package com.ab.eduplatform.entity;

import com.ab.eduplatform.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class UserIT {

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
    void shouldCreateUser() {
        User user = getUser();

        session.persist(user);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getFirstname()).isEqualTo("John");
        assertThat(user.getRole()).isEqualTo(Role.STUDENT);
    }

    @Test
    void shouldGetUser() {
        User user = getUser();
        session.persist(user);

        User retrievedUser = session.get(User.class, user.getId());

        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getFirstname()).isEqualTo("John");
        assertThat(retrievedUser.getRole()).isEqualTo(Role.STUDENT);
    }

    @Test
    void shouldUpdateUser() {
        User user = getUser();
        session.persist(user);
        user.setFirstname("updated firstname");
        user.setLastname("updated lastname");
        user.setRole(Role.TEACHER);

        session.merge(user);
        User updatedUser = session.get(User.class, user.getId());

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getFirstname()).isEqualTo("updated firstname");
        assertThat(updatedUser.getLastname()).isEqualTo("updated lastname");
        assertThat(updatedUser.getRole()).isEqualTo(Role.TEACHER);
    }

    @Test
    void shouldDeleteUser() {
        Long userId;
        User user = getUser();
        session.persist(user);
        userId = user.getId();

        session.remove(user);
        User deletedUser = session.get(User.class, userId);

        assertThat(deletedUser).isNull();
    }

    @Test
    void checkOneToOneUserWithProfile() {
        User user = getUser();
        Profile profile = Profile.builder()
                .phoneNumber("323-323-323")
                .bio("simple bio")
                .language("en")
                .build();
        user.setProfile(profile);

        session.persist(user);
        profile.setUser(user);
        session.persist(profile);
    }

    @Test
    void checkOneToManyUserWithCourses() {
        User user = User.builder()
                .firstname("Ara")
                .lastname("Ara")
                .email("araara@gmail.com")
                .password("123")
                .role(Role.TEACHER)
                .registrationDate(Instant.now())
                .build();
        Course courseJava = Course.builder()
                .name("java")
                .description("just java")
                .price(12.99)
                .level(Level.INTERMEDIATE)
                .category("programming")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .image("java.jpg")
                .build();
        Course coursePython = Course.builder()
                .name("python")
                .description("just python")
                .price(11.49)
                .level(Level.ADVANCED)
                .category("programming")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .image("python.jpg")
                .build();
        user.getCoursesTaught().addAll(Arrays.asList(coursePython, courseJava));

        session.persist(user);
        User savedUser = session.get(User.class, user.getId());
        session.persist(coursePython);
        session.persist(courseJava);
        Course retrievedCoursePython = session.get(Course.class, coursePython.getId());
        Course retrievedCourseJava = session.get(Course.class, courseJava.getId());

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getCoursesTaught()).hasSize(2);
        assertThat(savedUser.getCoursesTaught())
                .extracting(Course::getName)
                .contains("python");
        assertThat(savedUser.getCoursesTaught())
                .extracting(Course::getName)
                .contains("java");
        assertThat(retrievedCoursePython).isNotNull();
        assertThat(retrievedCourseJava).isNotNull();
        assertThat(retrievedCourseJava.getName()).isEqualTo("java");
        assertThat(retrievedCoursePython.getName()).isEqualTo("python");
    }

    private User getUser() {
        return User.builder()
                .firstname("John")
                .lastname("King")
                .email("johnking@gmail.com")
                .password("123")
                .role(Role.STUDENT)
                .registrationDate(Instant.now())
                .build();
    }
}