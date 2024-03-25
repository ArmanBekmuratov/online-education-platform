package com.ab.eduplatform.repository;

import com.ab.eduplatform.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CertificateRepository extends JpaRepository<Certificate, Long>, QuerydslPredicateExecutor<Certificate> {

}
