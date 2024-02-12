package com.ab;

import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import static java.time.LocalDate.now;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .firstname("test")
                    .lastname("test test")
                    .email("test@gmail.com")
                    .password("test")
                    .role(Role.STUDENT)
                    .registrationDate(now())
                    .build();
            session.persist(user);

            session.getTransaction().commit();
        }
    }
}