package com.ab.eduplatform.config;

import com.ab.eduplatform.util.HibernateTestUtil;
import com.ab.eduplatform.util.SessionProxyUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.testcontainers.shaded.com.google.errorprone.annotations.IncompatibleModifiers;

@Configuration
@ComponentScan("com.ab.eduplatform")
public class ApplicationConfigurationTest {

    @Bean(name = "testSessionFactory")
    public SessionFactory getSessionFactory() {
        return HibernateTestUtil.buildSessionFactory();
    }
}
