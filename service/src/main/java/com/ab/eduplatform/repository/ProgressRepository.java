package com.ab.eduplatform.repository;

import com.ab.eduplatform.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProgressRepository extends JpaRepository<Progress, Long>, QuerydslPredicateExecutor<Progress> {
}
