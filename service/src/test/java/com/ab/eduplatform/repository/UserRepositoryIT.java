package com.ab.eduplatform.repository;

import com.ab.eduplatform.entity.Course;
import com.ab.eduplatform.entity.Progress;
import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@RequiredArgsConstructor
class UserRepositoryIT extends IntegrationTestBase {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ProgressRepository progressRepository;

    @Test
    void findUserByName() {
        String targetFirstName = "TargetFirstname";
        String targetLastName = "TargetLastname ";
        User user = getUser();
        user.setFirstname(targetFirstName);
        user.setLastname(targetLastName);
        userRepository.save(user);

        List<User> users = userRepository.findUsers(targetFirstName, targetLastName);

        assertThat(users).hasSize(1);
    }

    @Test
    void findUsersByNameAndRole() {
        String targetFirstName = "TargetFirstname";
        String targetLastName = "TargetLastname ";
        Role teacherRole = Role.TEACHER;
        User user = getUser();
        user.setFirstname(targetFirstName);
        user.setLastname(targetLastName);
        user.setRole(teacherRole);
        userRepository.save(user);

        List<User> users = userRepository.findUsers(targetFirstName, targetLastName, teacherRole);

        assertThat(users).hasSize(1);
    }

    @Test
    void findAverageProgressGradeByFirstAndLastNames() {
        String targetFirstName = "TargetFirstname";
        String targetLastName = "TargetLastname ";
        User user = getUser();
        user.setFirstname(targetFirstName);
        user.setLastname(targetLastName);
        Course course = Course.builder()
                .build();
        Progress progress1 = getProgress();
        progress1.setUser(user);
        progress1.setCourse(course);
        progress1.setGrade(50);
        Progress progress2 = getProgress();
        progress2.setCourse(course);
        progress2.setUser(user);
        progress2.setGrade(60);
        userRepository.save(user);
        courseRepository.save(course);
        progressRepository.save(progress1);
        progressRepository.save(progress2);

        Double averageGrade = userRepository.findAverageProgressGrade(targetFirstName, targetLastName);

        assertThat(averageGrade).isCloseTo(55.0, within(0.001));
        assertThat(averageGrade).isBetween(54.0, 56.0);
    }

    @Test
    public void findAllPageable() {
        User user1 = User.builder()
                .firstname("user1")
                .lastname("user1")
                .build();
        User user2 = User.builder()
                .firstname("user2")
                .lastname("user2")
                .build();
        User user3 = User.builder()
                .firstname("user3")
                .lastname("user3")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        entityManager.flush();
        PageRequest request = PageRequest.of(0, 2);

        Page<User> page = userRepository.findAllBy(request);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    private Progress getProgress() {
        return Progress.builder()
                .completedLessons("25")
                .grade(100)
                .build();
    }

    private User getUser() {
        return User.builder()
                .firstname("John")
                .lastname("King")
                .email("johnking@gmail.com")
                .password("123")
                .role(Role.STUDENT)
                .registrationDate(Instant.now())
                .build();
    }
}