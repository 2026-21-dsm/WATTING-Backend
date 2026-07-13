package com.whatting.domain.user.service;

import com.whatting.domain.user.domain.Role;
import com.whatting.domain.user.domain.User;
import com.whatting.domain.user.exception.InvalidTeacherCodeException;
import com.whatting.domain.user.exception.StudentAlreadyExistsException;
import com.whatting.domain.user.exception.TeacherAlreadyExistsException;
import com.whatting.domain.user.presentation.dto.request.StudentSignupRequest;
import com.whatting.domain.user.presentation.dto.request.TeacherSignupRequest;
import com.whatting.domain.user.presentation.dto.response.UserResponse;
import com.whatting.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${teacher.code}")
    private String teacherCode;

    @Transactional
    public UserResponse signupStudent(StudentSignupRequest request) {
        if (userRepository.existsBySchoolNameAndGradeAndClassNumberAndStudentNumber(
                request.schoolName(),
                request.grade(),
                request.classNumber(),
                request.studentNumber()
        )) {
            throw StudentAlreadyExistsException.EXCEPTION;
        }

        User user = User.createStudent(
                request.schoolName(),
                request.grade(),
                request.classNumber(),
                request.studentNumber(),
                request.name(),
                passwordEncoder.encode(request.password())
        );

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse signupTeacher(TeacherSignupRequest request) {
        if (!teacherCode.equals(request.teacherCode())) {
            throw InvalidTeacherCodeException.EXCEPTION;
        }

        if (userRepository.existsBySchoolNameAndNameAndRole(request.schoolName(), request.name(), Role.TEACHER)) {
            throw TeacherAlreadyExistsException.EXCEPTION;
        }

        User user = User.createTeacher(
                request.schoolName(),
                request.name(),
                passwordEncoder.encode(request.password())
        );

        return UserResponse.from(userRepository.save(user));
    }
}
