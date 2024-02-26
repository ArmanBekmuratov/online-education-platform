package com.ab.eduplatform.dao;

import com.ab.eduplatform.dto.CategoryFilter;
import com.ab.eduplatform.entity.Course;
import com.ab.eduplatform.entity.Role;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;

import static com.ab.eduplatform.entity.QCourse.course;
import static com.ab.eduplatform.entity.QUser.*;

@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class CourseDao {

    private static final CourseDao INSTANCE = new CourseDao();

    /**
     * Search courses by category and level
     */
    public List<Course> findCoursesByCategoryAndLevel(Session session, CategoryFilter filter) {
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

    public List<Course> findCoursesByTeacherName(Session session, String teacherName) {;
        return new JPAQuery<Course>(session)
                .select(course)
                .from(course)
                .join(course.teacher, user)
                .where(user.firstname.concat(" ").concat(user.lastname).likeIgnoreCase("%" + teacherName + "%")
                        .and(user.role.eq(Role.TEACHER)))
                .fetch();
    }


    public static CourseDao getInstance() {
        return INSTANCE;
    }
}
