package com.ab.eduplatform.dao;

import com.ab.eduplatform.dao.repository.CertificateRepository;
import com.ab.eduplatform.dto.ProgressFilter;
import com.ab.eduplatform.entity.Certificate;
import com.ab.eduplatform.entity.Course;
import com.ab.eduplatform.entity.Level;
import com.ab.eduplatform.entity.Profile;
import com.ab.eduplatform.entity.Progress;
import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.entity.User;
import com.ab.eduplatform.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


class UserDaoIT {

    private static SessionFactory sessionFactory;
    private static Session session;
    private UserDao userDao;

    @BeforeAll
    static void openSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSessionAndTransaction() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        userDao = UserDao.getInstance();
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
    void findAllWithCriteriaApi() {
        User user1 = getUser();
        User user2 = getUser();
        user2.setEmail("test2@gmail.com");
        session.persist(user1);
        session.persist(user2);
        session.flush();

        List<User> users = userDao.findAllWithCriteriaApi(session);

        assertThat(users).hasSize(2);
        assertThat(users).containsAll(Arrays.asList(user1, user2));
    }

    @Test
    void findAllByFirstName() {
        String targetFirstName = "Test";
        User user1 = getUser();
        User user2 = getUser();
        user2.setEmail("test2@gmail.com");
        user2.setFirstname(targetFirstName);
        User user3 = getUser();
        user3.setFirstname("dummy name");
        user3.setEmail("test3Gmail.com");
        session.persist(user1);
        session.persist(user2);
        session.persist(user3);
        session.flush();

        List<User> users = userDao.findAllByFirstName(session, targetFirstName);

        assertThat(users).isNotNull();
        assertThat(users).hasSize(1);
        assertThat(users).contains(user2);
    }

    @Test
    void checkEntityGraph() {
        String targetFirstName = "Test";
        User user = getUser();
        user.setFirstname(targetFirstName);
        Progress progress = getProgress();
        progress.setUser(user);
        session.persist(user);
        session.persist(progress);
        session.flush();

        RootGraph<?> entityGraph = session.getEntityGraph("withProfileAndCoursesTaughtAndProgressAndCertificate");
        session.setProperty(GraphSemantic.FETCH.getJakartaHintName(), entityGraph);

        userDao.findAllByFirstName(session, targetFirstName);
     }

    @Test
    void findAllByFirstNameWithCriteriaApi() {
        String targetFirstName = "Test";
        User user1 = getUser();
        User user2 = getUser();
        user2.setEmail("test2@gmail.com");
        user2.setFirstname(targetFirstName);
        User user3 = getUser();
        user3.setFirstname("dummy name");
        user3.setEmail("test3Gmail.com");
        session.persist(user1);
        session.persist(user2);
        session.persist(user3);
        session.flush();

        List<User> users = userDao.findAllByFirstNameWithCriteriaApi(session, targetFirstName);

        assertThat(users).isNotNull();
        assertThat(users).hasSize(1);
        assertThat(users).contains(user2);
    }

    @Test
    void findAverageProgressGradeByFirstAndLastNames() {
        String targetFirstName = "TargetFirstname";
        String targetLastName = "TargetLastname ";
        User user = getUser();
        user.setFirstname(targetFirstName);
        user.setLastname(targetLastName);

        Progress progress1 = getProgress();
        progress1.setUser(user);
        progress1.setGrade(50);
        Progress progress2 = getProgress();
        progress2.setUser(user);
        progress2.setGrade(60);

        ProgressFilter progressFilter = ProgressFilter.builder()
                .firstname(targetFirstName)
                .lastname(targetLastName)
                .build();

        session.persist(progress1);
        session.persist(progress2);
        session.persist(user);
        session.flush();

        Double averageGrade = userDao.findAverageProgressGradeByFirstAndLastNames(session, progressFilter);

        assertThat(averageGrade).isCloseTo(55.0, within(0.001));
        assertThat(averageGrade).isBetween(54.0, 56.0);
    }

    @Test
    void findAverageProgressGradeByFirstAndLastNamesWithCriteriaApi() {
        String targetFirstName = "TargetFirstname";
        String targetLastName = "TargetLastname ";
        User user = getUser();
        user.setFirstname(targetFirstName);
        user.setLastname(targetLastName);

        Progress progress1 = getProgress();
        progress1.setUser(user);
        progress1.setGrade(50);
        Progress progress2 = getProgress();
        progress2.setUser(user);
        progress2.setGrade(60);

        ProgressFilter progressFilter = ProgressFilter.builder()
                .firstname(targetFirstName)
                .lastname(targetLastName)
                .build();

        session.persist(progress1);
        session.persist(progress2);
        session.persist(user);
        session.flush();

        Double averageGrade = userDao.findAverageProgressGradeByFirstAndLastNamesWithCriteriaApi(session, progressFilter);

        assertThat(averageGrade).isCloseTo(55.0, within(0.001));
        assertThat(averageGrade).isBetween(54.0, 56.0);
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

    private Progress getProgress() {
        return Progress.builder()
                .completedLessons("25")
                .grade(100)
                .build();
    }

}