package com.ab.eduplatform.repository;

import com.ab.eduplatform.dto.CategoryFilter;
import com.ab.eduplatform.entity.Course;
import com.ab.eduplatform.entity.Level;
import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class CourseRepositoryIT extends IntegrationTestBase {

    private final CourseRepository courseRepository;

    @Test
    void shouldCreateCourse() {
        Course course = getCourse();

        courseRepository.save(course);

        assertThat(course.getId()).isNotNull();
    }

    @Test
    void shouldGetCourse() {
        Course course = getCourse();
        courseRepository.save(course);

        Optional<Course> retrievedCourse = courseRepository.findById(course.getId());

        assertThat(retrievedCourse).isPresent();
        assertThat(retrievedCourse.get().getName()).isEqualTo("java");
        assertThat(retrievedCourse.get().getPrice()).isEqualTo(12.99);
    }

    @Test
    void shouldUpdateCourse() {
        Course course = getCourse();
        courseRepository.save(course);
        course.setCategory("updated category");
        course.setName("updated name");

        courseRepository.update(course);
        Optional<Course> updatedCourse = courseRepository.findById(course.getId());

        assertThat(updatedCourse).isPresent();
        assertThat(updatedCourse.get().getCategory()).isEqualTo("updated category");
        assertThat(updatedCourse.get().getName()).isEqualTo("updated name");
    }

    @Test
    void shouldDeleteCourse() {
        Course course = getCourse();
        courseRepository.save(course);

        courseRepository.delete(course);
        entityManager.detach(course);

        Optional<Course> deletedCourse = courseRepository.findById(course.getId());
        assertThat(deletedCourse).isEmpty();
    }

    @Test
    void shouldFindAllCourses() {
        Course course1 = getCourse();
        Course course2 = getCourse();
        Course course3 = getCourse();
        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);

        List<Course> courses = courseRepository.findAll();

        assertThat(courses).hasSize(3);
    }

    @Test
    void findCoursesByCategoryAndLevel() {
        CategoryFilter filter = CategoryFilter.builder()
                .category("programming")
                .level(Level.INTERMEDIATE)
                .build();
        Course expectedCourse = getCourse();
        entityManager.persist(expectedCourse);

        List<Course> courses = courseRepository.findCoursesByCategoryAndLevel(filter);

        assertThat(courses).isNotEmpty();
        assertThat(courses.get(0).getName()).isEqualTo(expectedCourse.getName());
    }

    @Test
    void findCoursesByTeacherName() {
        User teacher = getUser();
        entityManager.persist(teacher);
        Course expectedCourse = getCourse();
        expectedCourse.setTeacher(teacher);
        entityManager.persist(expectedCourse);

        List<Course> courses = courseRepository.findCoursesByTeacherName("John King");

        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getName()).isEqualTo(expectedCourse.getName());
    }

    @Test
    void findCoursesByPriceRange() {
        Course course = getCourse();
        entityManager.persist(course);

        List<Course> coursesByPriceRange = courseRepository.findCoursesByPriceRange(12.0, 13.0);

        assertThat(coursesByPriceRange).hasSize(1);
    }

    private User getUser() {
        return User.builder()
                .firstname("John")
                .lastname("King")
                .email("johnking@gmail.com")
                .password("123")
                .role(Role.TEACHER)
                .registrationDate(Instant.now())
                .build();
    }


    private Course getCourse() {
        return Course.builder()
                .name("java")
                .description("just java")
                .price(12.99)
                .level(Level.INTERMEDIATE)
                .category("programming")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .image("java.jpg")
                .build();
    }
}
