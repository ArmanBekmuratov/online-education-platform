package com.ab.eduplatform.dao.repository;

import com.ab.eduplatform.entity.Course;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

public class CourseRepository extends RepositoryBase<Long, Course>{

    public CourseRepository(Class<Course> clazz, Session session) {
        super(clazz, session);
    }
}
