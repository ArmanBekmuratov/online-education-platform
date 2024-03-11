package com.ab.eduplatform.repository;

import com.ab.eduplatform.dto.ProgressFilter;
import com.ab.eduplatform.dto.RoleFilter;
import com.ab.eduplatform.entity.Progress;
import com.ab.eduplatform.entity.User;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

import java.util.List;

import static com.ab.eduplatform.entity.QProgress.progress;
import static com.ab.eduplatform.entity.QUser.user;

@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class UserDao {

    private static final UserDao INSTANCE = new UserDao();



    public static UserDao getInstance() {
        return INSTANCE;
    }
}
