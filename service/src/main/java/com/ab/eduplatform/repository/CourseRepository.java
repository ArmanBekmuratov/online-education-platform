package com.ab.eduplatform.repository;

import com.ab.eduplatform.dto.CategoryFilter;
import com.ab.eduplatform.entity.Course;
import com.ab.eduplatform.entity.Role;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ab.eduplatform.entity.QCourse.course;
import static com.ab.eduplatform.entity.QUser.user;

@Repository
public class CourseRepository extends RepositoryBase<Long, Course>{

    public CourseRepository(EntityManager entityManager) {
        super(Course.class, entityManager);
    }

    public List<Course> findCoursesByCategoryAndLevel(EntityManager session, CategoryFilter filter) {
        Predicate predicate = QPredicate.builder()
                .add(filter.getCategory(), course.category::eq)
                .add(filter.getLevel(), course.level::eq)
                .buildAnd();

        return new JPAQuery<>(session)
                .select(course)
                .from(course)
                .where(predicate)
                .fetch();
    }

    public List<Course> findCoursesByTeacherName(EntityManager session, String teacherName) {
        return new JPAQuery<Course>(session)
                .select(course)
                .from(course)
                .join(course.teacher, user)
                .where(user.firstname.concat(" ").concat(user.lastname).likeIgnoreCase("%" + teacherName + "%")
                        .and(user.role.eq(Role.TEACHER)))
                .fetch();
    }

    public List<Course> findCoursesByPriceRange(EntityManager session, double minPrice, double maxPrice) {
        return new JPAQuery<Course>(session)
                .select(course)
                .from(course)
                .where(course.price.between(minPrice, maxPrice))
                .fetch();
    }
}
