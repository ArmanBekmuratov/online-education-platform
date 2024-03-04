package com.ab.eduplatform.dao.repository;

import com.ab.eduplatform.entity.Progress;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

public class ProgressRepository extends RepositoryBase<Long, Progress> {

    public ProgressRepository(Class<Progress> clazz, Session session) {
        super(clazz, session);
    }
}
