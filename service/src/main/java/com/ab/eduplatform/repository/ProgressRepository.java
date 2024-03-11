package com.ab.eduplatform.repository;

import com.ab.eduplatform.entity.Progress;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class ProgressRepository extends RepositoryBase<Long, Progress> {

    public ProgressRepository(EntityManager entityManager) {
        super(Progress.class, entityManager);
    }
}
