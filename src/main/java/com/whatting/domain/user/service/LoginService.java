package com.whatting.domain.user.service;

import com.whatting.domain.user.domain.User;
import com.whatting.domain.user.exception.PasswordMisMatchException;
import com.whatting.domain.user.exception.UserNotFoundException;
import com.whatting.domain.user.presentation.dto.request.AuthRequest;
import com.whatting.domain.user.presentation.dto.response.TokenWithRoleResponse;
import com.whatting.domain.user.repository.UserRepository;
import com.whatting.global.security.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenWithRoleResponse execute(AuthRequest authRequest) {
        User user = userRepository.findByName(authRequest.name())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if(!passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            throw PasswordMisMatchException.EXCEPTION;
        }

        return jwtTokenProvider.generateBothToken(authRequest.name(), user.getRole());
    }
}
