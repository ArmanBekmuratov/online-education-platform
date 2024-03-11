package com.ab.eduplatform.repository;

import com.ab.eduplatform.entity.Lesson;
import com.ab.eduplatform.repository.LessonRepository;
import com.ab.eduplatform.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.ab.eduplatform.repository.RepositoryBaseIT.context;
import static com.ab.eduplatform.repository.RepositoryBaseIT.entityManager;
import static org.assertj.core.api.Assertions.assertThat;

class LessonRepositoryIT extends RepositoryBaseIT {

    private static LessonRepository lessonRepository;

    @BeforeAll
    static void init() {
        lessonRepository = context.getBean("lessonRepository", LessonRepository.class);
    }

    @Test
    void shouldCreateLesson() {
        Lesson lesson = getLesson();

        lessonRepository.save(lesson);

        assertThat(lesson.getId()).isNotNull();
    }

    @Test
    void shouldGetLesson() {
        Lesson lesson = getLesson();
        lessonRepository.save(lesson);

        Optional<Lesson> retrievedLesson = lessonRepository.findById(lesson.getId());

        assertThat(retrievedLesson).isPresent();
    }

    @Test
    void shouldUpdateLesson() {
        Lesson lesson = getLesson();
        lessonRepository.save(lesson);
        lesson.setDescription("updated description");
        lesson.setName("updated name");
        lesson.setOrderNumber(10000);

        lessonRepository.update(lesson);
        Optional<Lesson> updatedLesson = lessonRepository.findById(lesson.getId());

        assertThat(updatedLesson).isPresent();
        assertThat(lesson.getOrderNumber()).isEqualTo(10000);
        assertThat(lesson.getDescription()).isEqualTo("updated description");
        assertThat(lesson.getName()).isEqualTo("updated name");
    }

    @Test
    void shouldDeleteLesson() {
        Lesson lesson = getLesson();
        lessonRepository.save(lesson);

        lessonRepository.delete(lesson);
        entityManager.detach(lesson);

        Optional<Lesson> deletedLesson = lessonRepository.findById(lesson.getId());
        assertThat(deletedLesson).isEmpty();
    }

    @Test
    void shouldFindAllLessons() {
        Lesson lesson1 = getLesson();
        Lesson lesson2 = getLesson();
        Lesson lesson3 = getLesson();
        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);
        lessonRepository.save(lesson3);

        List<Lesson> lessons = lessonRepository.findAll();

        assertThat(lessons).hasSize(3);
    }

    private Lesson getLesson() {
        return Lesson.builder()
                .name("first")
                .description("some info")
                .video("video.mp4")
                .slides("first")
                .test("test")
                .orderNumber(100)
                .build();
    }
}
