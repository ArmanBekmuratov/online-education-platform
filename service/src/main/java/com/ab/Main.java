package com.ab;

import com.ab.eduplatform.config.ApplicationConfiguration;
import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.entity.User;
import com.ab.eduplatform.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
    }
}