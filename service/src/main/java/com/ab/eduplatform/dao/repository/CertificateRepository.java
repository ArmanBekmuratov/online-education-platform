package com.ab.eduplatform.dao.repository;

import com.ab.eduplatform.entity.Certificate;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

public class CertificateRepository extends RepositoryBase<Long, Certificate> {

    public CertificateRepository(Class<Certificate> clazz, Session session) {
        super(clazz, session);
    }
}
