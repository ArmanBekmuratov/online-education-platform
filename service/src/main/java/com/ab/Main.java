package com.ab;

import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.entity.User;
import com.ab.eduplatform.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .firstname("test")
                    .lastname("test test")
                    .email("test@gmail.com")
                    .password("test")
                    .role(Role.STUDENT)
                    .registrationDate(Instant.now())
                    .build();
            session.persist(user);

            session.getTransaction().commit();
        }
    }
}