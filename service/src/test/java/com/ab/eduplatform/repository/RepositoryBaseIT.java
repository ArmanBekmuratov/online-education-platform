package com.ab.eduplatform.repository;

import com.ab.eduplatform.config.ApplicationConfiguration;
import com.ab.eduplatform.config.ApplicationConfigurationTest;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.internal.AnnotatedClassType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class RepositoryBaseIT {

    protected static SessionFactory sessionFactory;
    protected static EntityManager entityManager;
    protected static AnnotationConfigApplicationContext context;

    @BeforeAll
    static void openSessionFactory() {
        context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        entityManager = context.getBean("getEntityManager", EntityManager.class);
        sessionFactory = context.getBean("testSessionFactory", SessionFactory.class);
    }

    @BeforeEach
    void openSessionAndTransaction() {
        entityManager = sessionFactory.openSession();
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void closeSessionAndTransaction() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
        entityManager.close();
    }

    @AfterAll
    static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
            context.close();
        }
    }
}

