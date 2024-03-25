package com.ab.eduplatform.repository;

import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    @Query("select u from User u where u." +
            "firstname = :firstname and u.lastname = :lastname")
    List<User> findUsers(String firstname, String lastname);

    @Query("select avg (p.grade) from Progress p " +
            "join p.user u " +
            "where u.firstname = :firstname and u.lastname = :lastname")
    Double findAverageProgressGrade(String firstname, String lastname);

    @Query("select u from User u where u.firstname = :firstname and u.lastname = :lastname and u.role = :role")
    List<User> findUsers(String firstname, String lastname, Role role);

    @Query(value = "select u from User u",
            countQuery = "select count(distinct u.firstname) from User u")
    Page<User> findAllBy(Pageable pageable);
}
