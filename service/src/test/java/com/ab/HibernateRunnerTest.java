package com.ab;

import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HibernateRunnerTest {
    private SessionFactory sessionFactory;
    private Session session;

    @BeforeEach
    public void initConfiguration() {
        Configuration configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    @Test
    public void shouldCreateUser() {
        Transaction transaction = session.beginTransaction();

        User user = User.builder()
                .firstname("test")
                .lastname("test test")
                .email("test@gmail.com")
                .password("test")
                .role(Role.STUDENT)
                .registrationDate(now())
                .build();

        session.persist(user);

        transaction.commit();

        User getUser = session.get(User.class, user.getId());

        assertEquals("test test", getUser.getLastname());
    }
}
