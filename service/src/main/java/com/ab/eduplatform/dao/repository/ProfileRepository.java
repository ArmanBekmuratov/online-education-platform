package com.ab.eduplatform.dao.repository;

import com.ab.eduplatform.entity.Profile;
import org.hibernate.Session;

public class ProfileRepository extends RepositoryBase<Long, Profile> {

    public ProfileRepository(Class<Profile> clazz, Session session) {
        super(clazz, session);
    }
}
