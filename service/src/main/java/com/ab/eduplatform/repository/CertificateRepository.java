package com.ab.eduplatform.repository;

import com.ab.eduplatform.entity.Certificate;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class CertificateRepository extends RepositoryBase<Long, Certificate> {

    public CertificateRepository(EntityManager entityManager) {
        super(Certificate.class, entityManager);
    }
}
