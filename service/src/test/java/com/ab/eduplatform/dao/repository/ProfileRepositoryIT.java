package com.ab.eduplatform.dao.repository;

import com.ab.eduplatform.entity.Lesson;
import com.ab.eduplatform.entity.Profile;
import com.ab.eduplatform.entity.User;
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

import static org.assertj.core.api.Assertions.assertThat;

class ProfileRepositoryIT {

    private static SessionFactory sessionFactory;
    private static Session session;
    private ProfileRepository profileRepository;

    @BeforeAll
    static void openSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void openSessionAndTransaction() {
        session = sessionFactory.openSession();
        session.beginTransaction();
        profileRepository = new ProfileRepository(Profile.class, session);
    }

    @AfterEach
    void closeSessionAndTransaction() {
        if (session.getTransaction().isActive()) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    @AfterAll
    static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    void shouldCreateProfile() {
        Profile profile = getProfile();

        profileRepository.save(profile);

        assertThat(profile.getId()).isNotNull();
    }

    @Test
    void shouldGetProfile() {
        Profile profile = getProfile();
        profileRepository.save(profile);

        Optional<Profile> retrievedProfile = profileRepository.findById(profile.getId());

        assertThat(retrievedProfile).isPresent();
        assertThat(retrievedProfile.get().getBio()).isEqualTo("simple bio");
        assertThat(retrievedProfile.get().getLanguage()).isEqualTo("en");
    }

    @Test
    void shouldUpdateProfile() {
        Profile profile = getProfile();
        profileRepository.save(profile);
        profile.setBio("updated bio");
        profile.setLanguage("updated language");

        profileRepository.update(profile);
        Optional<Profile> retrievedProfile = profileRepository.findById(profile.getId());

        assertThat(retrievedProfile).isPresent();
        assertThat(retrievedProfile.get().getBio()).isEqualTo("updated bio");
        assertThat(retrievedProfile.get().getLanguage()).isEqualTo("updated language");
    }

    @Test
    void shouldDeleteProfile() {
        Profile profile = getProfile();
        profileRepository.save(profile);

        profileRepository.delete(profile.getId());
        session.evict(profile);

        Optional<Profile> deletedProfile = profileRepository.findById(profile.getId());
        assertThat(deletedProfile).isEmpty();
    }

    @Test
    void shouldFindAllProfiles() {
        Profile profile1 = getProfile();
        Profile profile2 = getProfile();
        Profile profile3 = getProfile();
        profileRepository.save(profile1);
        profileRepository.save(profile2);
        profileRepository.save(profile3);

        List<Profile> profiles = profileRepository.findAll();

        assertThat(profiles).hasSize(3);
    }

    private  Profile getProfile() {
        return Profile.builder()
                .phoneNumber("323-323-323")
                .bio("simple bio")
                .language("en")
                .build();
    }
}
