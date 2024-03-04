package com.ab.eduplatform.dao.repository;

import com.ab.eduplatform.entity.User;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

public class UserRepository extends RepositoryBase<Long, User> {

    public UserRepository(Class<User> clazz, Session session) {
        super(clazz, session);
    }
}
