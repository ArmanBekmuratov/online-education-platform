package com.ab.eduplatform.repository;

import com.ab.eduplatform.entity.Lesson;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class LessonRepository extends RepositoryBase<Long, Lesson> {

    public LessonRepository(EntityManager entityManager) {
        super(Lesson.class, entityManager);
    }
}
