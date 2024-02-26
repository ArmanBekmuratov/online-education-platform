package com.ab.eduplatform.dao;

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

    /**
     *  Return all users
     */
    public List<User> findAll(Session session) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .fetch();
    }

    /**
     *  Return all users but with CriteriaAPI
     */
    public List<User> findAllWithCriteriaApi(Session session) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();

        JpaCriteriaQuery<User> criteria = cb.createQuery(User.class);
        JpaRoot<User> user = criteria.from(User.class);

        criteria.select(user);

        return session.createQuery(criteria)
                .list();
    }

    /**
     *  Return all users by their firstname
     */
    public List<User> findAllByFirstName(Session session, String firstname) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.firstname.eq(firstname))
                .fetch();
    }

    /**
     *  Return all users by their firstname but with CriteriaAPI
     */
    public List<User> findAllByFirstNameWithCriteriaApi(Session session, String firstname) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();

        JpaCriteriaQuery<User> criteria = cb.createQuery(User.class);
        JpaRoot<User> user = criteria.from(User.class);

        criteria.select(user).where(
                cb.equal(user.get("firstname"), firstname)
        );

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Return average grade of user by firstname and lastname using ProgressFilter
     */
    public Double findAverageProgressGradeByFirstAndLastNames(Session session, ProgressFilter filter) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getFirstname(), user.firstname::eq)
                .add(filter.getLastname(), user.lastname::eq)
                .buildAnd();

        return new JPAQuery<Double>(session)
                .select(progress.grade.avg())
                .from(progress)
                .join(progress.user, user)
                .where(predicate)
                .fetchFirst();
    }

    /**
     * Return average grade of user by firstname and lastname using ProgressFilter
     * but with CriteriaAPI
     */
    public Double findAverageProgressGradeByFirstAndLastNamesWithCriteriaApi(Session session, ProgressFilter filter) {
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Double> criteria = cb.createQuery(Double.class);

        Root<Progress> progress = criteria.from(Progress.class);
        Join<Progress, User> user = progress.join("user");

        if (filter.getFirstname() != null) {
            criteria.where(cb.equal(user.get("firstname"), filter.getFirstname()));
        }
        if (filter.getLastname() != null) {
            criteria.where(cb.equal(user.get("lastname"), filter.getLastname()));
        }

        criteria.select(cb.avg(progress.get("grade")));

        return session.createQuery(criteria).getSingleResult();
    }

    /**
     * Return users by their firstname, lastname, role and registration date
     */
    public List<User> findUsersByNameAndRoleAndRegistrationDate(Session session, RoleFilter filter)  {
        Predicate predicate = QPredicate.builder()
                .add(filter.getFirstname(), user.firstname::eq)
                .add(filter.getLastname(), user.lastname::eq)
                .add(filter.getRole(), user.role::eq)
                .add(filter.getRegistrationDate(), user.registrationDate::after)
                .buildAnd();

        return new JPAQuery<>(session)
                .select(user)
                .from(user)
                .where(predicate)
                .fetch();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
