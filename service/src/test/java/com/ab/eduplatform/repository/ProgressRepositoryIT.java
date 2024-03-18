package com.ab.eduplatform.repository;

import com.ab.eduplatform.entity.Progress;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class ProgressRepositoryIT extends IntegrationTestBase {

    private final ProgressRepository progressRepository;

    @Test
    void shouldCreateProgress() {
        Progress progress = getProgress();

        progressRepository.save(progress);

        assertThat(progress.getId()).isNotNull();
    }

    @Test
    void shouldGetProgress() {
        Progress progress = getProgress();
        progressRepository.save(progress);

        Optional<Progress> retrievedProgress = progressRepository.findById(progress.getId());

        assertThat(retrievedProgress).isPresent();
        assertThat(retrievedProgress.get().getGrade()).isEqualTo(100);
        assertThat(retrievedProgress.get().getCompletedLessons()).isEqualTo("25");
    }

    @Test
    void shouldUpdateProgress() {
        Progress progress = getProgress();
        progressRepository.save(progress);
        progress.setGrade(0);
        progress.setCompletedLessons("updated number");

        progressRepository.update(progress);
        Optional<Progress> updatedProgress = progressRepository.findById(progress.getId());

        assertThat(updatedProgress).isPresent();
        assertThat(updatedProgress.get().getGrade()).isEqualTo(0);
        assertThat(updatedProgress.get().getCompletedLessons()).isEqualTo("updated number");
    }

    @Test
    void shouldDeleteProgress() {
        Progress progress = getProgress();
        progressRepository.save(progress);

        progressRepository.delete(progress);
        entityManager.detach(progress);

        Optional<Progress> deletedProgress = progressRepository.findById(progress.getId());
        assertThat(deletedProgress).isEmpty();
    }

    @Test
    void shouldFindAllProgresses() {
        Progress progress1 = getProgress();
        Progress progress2 = getProgress();
        Progress progress3 = getProgress();
        progressRepository.save(progress1);
        progressRepository.save(progress2);
        progressRepository.save(progress3);

        List<Progress> progresses = progressRepository.findAll();

        assertThat(progresses).hasSize(3);
    }

    private Progress getProgress() {
        return Progress.builder()
                .completedLessons("25")
                .grade(100)
                .build();
    }
}
