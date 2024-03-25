package com.ab.eduplatform.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "withProfileAndCoursesTaughtAndProgressAndCertificate",
        attributeNodes = {
                @NamedAttributeNode("profile"),
                @NamedAttributeNode(value = "coursesTaught"),
                @NamedAttributeNode(value = "progress", subgraph = "progressSubgraph"),
                @NamedAttributeNode(value = "certificates")
        },
        subgraphs = @NamedSubgraph(
                name = "progressSubgraph",
                attributeNodes = @NamedAttributeNode("grade")
        )
)
@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(of = "email")
@ToString(exclude = {"profile", "coursesTaught", "progress", "certificates"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "registration_date")
    private Instant registrationDate;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            optional = false
    )
    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Course> coursesTaught = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Progress> progress = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Certificate> certificates = new ArrayList<>();
}
