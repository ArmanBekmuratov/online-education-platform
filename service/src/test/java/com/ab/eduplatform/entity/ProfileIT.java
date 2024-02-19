package com.ab.eduplatform.entity;

import com.ab.eduplatform.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileIT {

    private Session session;
    private Transaction transaction;

    @BeforeEach
    void openSession() {
        session = HibernateTestUtil.buildSessionFactory().openSession();
        transaction = session.beginTransaction();
    }

    @Test
    void shouldCreateProfile() {
        Profile profile = getProfile();

        session.persist(profile);

        assertThat(profile.getId()).isNotNull();
        assertThat(profile.getBio()).isEqualTo("simple bio");
    }

    @Test
    void shouldGetProfile() {
        Profile profile = getProfile();
        session.persist(profile);

        Profile retrievedProfile = session.get(Profile.class, profile.getId());

        assertThat(retrievedProfile).isNotNull();
        assertThat(retrievedProfile.getBio()).isEqualTo("simple bio");
        assertThat(retrievedProfile.getLanguage()).isEqualTo("en");
    }

    @Test
    void shouldUpdateProfile() {
        Profile profile = getProfile();
        session.persist(profile);
        profile.setBio("updated bio");
        profile.setLanguage("updated language");

        session.merge(profile);
        Profile retrievedProfile = session.get(Profile.class, profile.getId());

        assertThat(retrievedProfile).isNotNull();
        assertThat(retrievedProfile.getBio()).isEqualTo("updated bio");
        assertThat(retrievedProfile.getLanguage()).isEqualTo("updated language");
    }

    @Test
    void shouldDeleteProfile() {
        Long profileId;
        Profile profile = getProfile();
        session.persist(profile);
        profileId = profile.getId();

        session.remove(profile);
        Profile deletedProfile = session.get(Profile.class, profileId);

        assertThat(deletedProfile).isNull();
    }

    @AfterEach
    void closeSession() {
        if (transaction != null) {
            transaction.commit();
        }

        if (session != null) {
            session.close();
        }
    }

    private  Profile getProfile() {
        return Profile.builder()
                .phoneNumber("323-323-323")
                .bio("simple bio")
                .language("en")
                .build();
    }
}
