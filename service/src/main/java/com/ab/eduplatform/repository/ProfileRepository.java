package com.ab.eduplatform.repository;

import com.ab.eduplatform.entity.Profile;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.parser.Entity;

@Repository
public class ProfileRepository extends RepositoryBase<Long, Profile> {

    public ProfileRepository(EntityManager entityManager) {
        super(Profile.class, entityManager);
    }
}
