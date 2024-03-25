package com.ab.eduplatform.repository;

import com.ab.eduplatform.entity.Course;
import com.ab.eduplatform.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long>, QuerydslPredicateExecutor<Course> {

    @Query("select c from Course c join c.teacher t where concat(t.firstname, ' ', t.lastname) " +
            "like %:teacherName% and t.role = :role")
    List<Course> findCoursesBy(@Param("teacherName") String teacherName, @Param("role") Role role);

    @Query("select c from Course c where c.category = :category and c.level = :level")
    List<Course> findCoursesBy(String category, String level);

    @Query("select c from Course c where c.price between :minPrice and :maxPrice")
    List<Course> findCoursesBetween(double minPrice, double maxPrice);
}
