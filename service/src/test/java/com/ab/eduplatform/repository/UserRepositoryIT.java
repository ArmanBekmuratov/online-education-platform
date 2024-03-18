package com.ab.eduplatform.repository;

import com.ab.eduplatform.dto.ProgressFilter;
import com.ab.eduplatform.entity.Progress;
import com.ab.eduplatform.entity.Role;
import com.ab.eduplatform.entity.User;
import jakarta.persistence.EntityGraph;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@RequiredArgsConstructor
class UserRepositoryIT extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void shouldCreateUser() {
        User user = getUser();

        userRepository.save(user);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getFirstname()).isEqualTo("John");
        assertThat(user.getRole()).isEqualTo(Role.STUDENT);
    }

    @Test
    void shouldGetUser() {
        User user = getUser();
        userRepository.save(user);

        Optional<User> retrievedUser = userRepository.findById(user.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getFirstname()).isEqualTo("John");
        assertThat(retrievedUser.get().getRole()).isEqualTo(Role.STUDENT);
    }

    @Test
    void shouldUpdateUser() {
        User user = getUser();
        userRepository.save(user);
        user.setFirstname("updated firstname");
        user.setLastname("updated lastname");
        user.setRole(Role.TEACHER);

        userRepository.update(user);
        Optional<User> updatedUser = userRepository.findById(user.getId());
        entityManager.flush();

        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getFirstname()).isEqualTo("updated firstname");
        assertThat(updatedUser.get().getLastname()).isEqualTo("updated lastname");
        assertThat(updatedUser.get().getRole()).isEqualTo(Role.TEACHER);
    }

    @Test
    void shouldDeleteUser() {
        User user = getUser();
        userRepository.save(user);

        userRepository.delete(user);
        entityManager.flush();

        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void shouldFindAllUsers() {
        User user1 = getUser();
        User user2 = getUser();
        user2.setEmail("users2@gmail.com");
        User user3 = getUser();
        user3.setEmail("user3@gmail.com");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(3);
    }

    @Test
    void findAllWithCriteriaApi() {
        User user1 = getUser();
        User user2 = getUser();
        user2.setEmail("test2@gmail.com");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        List<User> users = userRepository.findAllWithCriteriaApi();

        assertThat(users).hasSize(2);
        assertThat(users).containsAll(Arrays.asList(user1, user2));
    }

    @Test
    void findAllByFirstName() {
        String targetFirstName = "Test";
        User user1 = getUser();
        User user2 = getUser();
        user2.setEmail("test2@gmail.com");
        user2.setFirstname(targetFirstName);
        User user3 = getUser();
        user3.setFirstname("dummy name");
        user3.setEmail("test3Gmail.com");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        List<User> users = userRepository.findAllByFirstName(targetFirstName);

        assertThat(users).isNotNull();
        assertThat(users).hasSize(1);
        assertThat(users).contains(user2);
    }

    @Test
    void checkEntityGraph() {
        String targetFirstName = "Test";
        User user = getUser();
        user.setFirstname(targetFirstName);
        Progress progress = getProgress();
        progress.setUser(user);
        entityManager.persist(user);
        entityManager.persist(progress);
        entityManager.flush();

        EntityGraph<?> entityGraph = entityManager.getEntityGraph("withProfileAndCoursesTaughtAndProgressAndCertificate");
        entityManager.setProperty(GraphSemantic.FETCH.getJakartaHintName(), entityGraph);

        userRepository.findAllByFirstName(targetFirstName);
    }

    @Test
    void findAllByFirstNameWithCriteriaApi() {
        String targetFirstName = "Test";
        User user1 = getUser();
        User user2 = getUser();
        user2.setEmail("test2@gmail.com");
        user2.setFirstname(targetFirstName);
        User user3 = getUser();
        user3.setFirstname("dummy name");
        user3.setEmail("test3Gmail.com");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        List<User> users = userRepository.findAllByFirstNameWithCriteriaApi(targetFirstName);

        assertThat(users).isNotNull();
        assertThat(users).hasSize(1);
        assertThat(users).contains(user2);
    }

    @Test
    void findAverageProgressGradeByFirstAndLastNames() {
        String targetFirstName = "TargetFirstname";
        String targetLastName = "TargetLastname ";
        User user = getUser();
        user.setFirstname(targetFirstName);
        user.setLastname(targetLastName);

        Progress progress1 = getProgress();
        progress1.setUser(user);
        progress1.setGrade(50);
        Progress progress2 = getProgress();
        progress2.setUser(user);
        progress2.setGrade(60);

        ProgressFilter progressFilter = ProgressFilter.builder()
                .firstname(targetFirstName)
                .lastname(targetLastName)
                .build();

        entityManager.persist(progress1);
        entityManager.persist(progress2);
        entityManager.persist(user);
        entityManager.flush();

        Double averageGrade = userRepository.findAverageProgressGradeByFirstAndLastNames(progressFilter);

        assertThat(averageGrade).isCloseTo(55.0, within(0.001));
        assertThat(averageGrade).isBetween(54.0, 56.0);
    }

    @Test
    void findAverageProgressGradeByFirstAndLastNamesWithCriteriaApi() {
        String targetFirstName = "TargetFirstname";
        String targetLastName = "TargetLastname ";
        User user = getUser();
        user.setFirstname(targetFirstName);
        user.setLastname(targetLastName);

        Progress progress1 = getProgress();
        progress1.setUser(user);
        progress1.setGrade(50);
        Progress progress2 = getProgress();
        progress2.setUser(user);
        progress2.setGrade(60);

        ProgressFilter progressFilter = ProgressFilter.builder()
                .firstname(targetFirstName)
                .lastname(targetLastName)
                .build();

        entityManager.persist(progress1);
        entityManager.persist(progress2);
        entityManager.persist(user);
        entityManager.flush();

        Double averageGrade = userRepository.findAverageProgressGradeByFirstAndLastNamesWithCriteriaApi(progressFilter);

        assertThat(averageGrade).isCloseTo(55.0, within(0.001));
        assertThat(averageGrade).isBetween(54.0, 56.0);
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