package com.ab.eduplatform.config;

import com.ab.eduplatform.util.HibernateUtil;
import com.ab.eduplatform.util.SessionProxyUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages =  "com.ab.eduplatform")
public class ApplicationConfiguration {

    @Bean
    public SessionFactory getSessionFactory() {
        return HibernateUtil.buildSessionFactory();
    }

    @Bean
    public EntityManager getEntityManager(SessionFactory sessionFactory) {
        return SessionProxyUtil.getSession(sessionFactory);
    }
}

