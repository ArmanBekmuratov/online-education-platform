package com.ab.eduplatform.repository;

import com.ab.eduplatform.dto.ProgressFilter;
import com.ab.eduplatform.dto.RoleFilter;
import com.ab.eduplatform.entity.Progress;
import com.ab.eduplatform.entity.User;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ab.eduplatform.entity.QProgress.progress;
import static com.ab.eduplatform.entity.QUser.user;

@Repository
public class UserRepository extends RepositoryBase<Long, User> {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    /**
     *  Return all users but with CriteriaAPI
     */
    public List<User> findAllWithCriteriaApi(EntityManager session) {
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        criteria.select(user);

        return session.createQuery(criteria)
                .getResultList();
    }

    /**
     *  Return all users by their firstname
     */
    public List<User> findAllByFirstName(EntityManager session, String firstname) {
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.firstname.eq(firstname))
                .fetch();
    }

    /**
     *  Return all users by their firstname but with CriteriaAPI
     */
    public List<User> findAllByFirstNameWithCriteriaApi(EntityManager session, String firstname) {
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        criteria.select(user).where(
                cb.equal(user.get("firstname"), firstname)
        );

        return session.createQuery(criteria)
                .getResultList();
    }

    /**
     * Return average grade of user by firstname and lastname using ProgressFilter
     */
    public Double findAverageProgressGradeByFirstAndLastNames(EntityManager session, ProgressFilter filter) {
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
    public Double findAverageProgressGradeByFirstAndLastNamesWithCriteriaApi(EntityManager session, ProgressFilter filter) {
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Double> criteria = cb.createQuery(Double.class);

        Root<Progress> progress = criteria.from(Progress.class);
        Join<Progress, User> user = progress.join("user");

        CriteriaPredicate criteriaPredicate = CriteriaPredicate.builder(cb)
                .add(filter.getFirstname(), name -> cb.equal(user.get("firstname"), name))
                .add(filter.getLastname(), name -> cb.equal(user.get("lastname"), name));

        criteria.where(criteriaPredicate.buildAnd());
        criteria.select(cb.avg(progress.get("grade")));

        return session.createQuery(criteria).getSingleResult();
    }

    /**
     * Return users by their firstname, lastname, role and registration date
     */
    public List<User> findUsersByNameAndRoleAndRegistrationDate(EntityManager session, RoleFilter filter)  {
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
}
