package com.whatting.domain.user.repository;

import com.whatting.domain.user.domain.User;
import com.whatting.domain.user.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    Optional<User> findByUserId(UUID userId);

    List<User> findAllByRole(Role role);

    boolean existsBySchoolNameAndGradeAndClassNumberAndStudentNumber(
            String schoolName,
            Integer grade,
            Integer classNumber,
            Integer studentNumber
    );

    boolean existsBySchoolNameAndNameAndRole(String schoolName, String name, Role role);
}
