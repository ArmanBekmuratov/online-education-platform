package com.ab.eduplatform.dao.repository;

import com.ab.eduplatform.entity.Lesson;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

public class LessonRepository extends RepositoryBase<Long, Lesson> {

    public LessonRepository(Class<Lesson> clazz, Session session) {
        super(clazz, session);
    }
}
