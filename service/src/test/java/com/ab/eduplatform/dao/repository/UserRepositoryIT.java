package com.ab.eduplatform.dao.repository;

import com.ab.eduplatform.entity.Course;
import com.ab.eduplatform.entity.Level;
import com.ab.eduplatform.entity.Profile;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryIT {

    private static SessionFactory sessionFactory;
    private static Session session;
    private UserRepository userRepository;

    @BeforeAll
    static void openSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSessionAndTransaction() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        userRepository = new UserRepository(User.class, session);
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
    void shouldCreateUser() {
        User user = getUser();

        userRepository.save(user);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getFirstname()).isEqualTo("John");
        assertThat(user.getRole()).isEqualTo(Role.STUDENT);
    }

    @Test
    void shouldGetUser() {
        User user = getUser();
        userRepository.save(user);

        Optional<User> retrievedUser = userRepository.findById(user.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getFirstname()).isEqualTo("John");
        assertThat(retrievedUser.get().getRole()).isEqualTo(Role.STUDENT);
    }

    @Test
    void shouldUpdateUser() {
        User user = getUser();
        userRepository.save(user);
        user.setFirstname("updated firstname");
        user.setLastname("updated lastname");
        user.setRole(Role.TEACHER);

        userRepository.update(user);
        Optional<User> updatedUser = userRepository.findById(user.getId());
        session.evict(user);

        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getFirstname()).isEqualTo("updated firstname");
        assertThat(updatedUser.get().getLastname()).isEqualTo("updated lastname");
        assertThat(updatedUser.get().getRole()).isEqualTo(Role.TEACHER);
    }

    @Test
    void shouldDeleteUser() {
        User user = getUser();
        userRepository.save(user);

        userRepository.delete(user.getId());
        session.evict(user);

        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void shouldFindAllUsers() {
        User user1 = getUser();
        User user2 = getUser();
        user2.setEmail("users2@gmail.com");
        User user3 = getUser();
        user3.setEmail("user3@gmail.com");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(3);
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