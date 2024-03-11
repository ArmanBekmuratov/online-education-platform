package com.ab.eduplatform.util;

import jakarta.persistence.EntityManager;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Proxy;

@UtilityClass
public class SessionProxyUtil {

    @Bean
    public static EntityManager getSession(SessionFactory sessionFactory) {
        return  (EntityManager) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
    }
}
