package com.whatting.domain.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(
        name = "user_tbl",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_identity",
                        columnNames = {"school_name", "grade", "class_number", "student_number"}
                ),
                @UniqueConstraint(
                        name = "uk_teacher_identity",
                        columnNames = {"school_name", "name", "role"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    private UUID userId;

    @Column(name = "school_name", nullable = false, length = 100)
    private String schoolName;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "class_number")
    private Integer classNumber;

    @Column(name = "student_number")
    private Integer studentNumber;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @PrePersist
    private void generateUserId() {
        if (userId == null) {
            userId = UUID.randomUUID();
        }
    }
}
