package com.whatting.domain.user.service;

import com.whatting.domain.user.domain.RefreshToken;
import com.whatting.domain.user.domain.Role;
import com.whatting.domain.user.domain.User;
import com.whatting.domain.user.exception.RefreshTokenMisMatchException;
import com.whatting.domain.user.exception.RefreshTokenNotFoundException;
import com.whatting.domain.user.exception.UserNotFoundException;
import com.whatting.domain.user.presentation.dto.request.RefreshTokenRequest;
import com.whatting.domain.user.presentation.dto.response.TokenWithRoleResponse;
import com.whatting.domain.user.repository.RefreshTokenRepository;
import com.whatting.domain.user.repository.UserRepository;
import com.whatting.global.security.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public TokenWithRoleResponse execute(RefreshTokenRequest request) {
        jwtTokenProvider.validateToken(request.refreshToken());

        Authentication authentication = jwtTokenProvider.getAuthentication(request.refreshToken());
        String name = authentication.getName();
        RefreshToken savedRefreshToken = refreshTokenRepository.findById(name)
                .orElseThrow(() -> RefreshTokenNotFoundException.EXCEPTION);

        if(!savedRefreshToken.getToken().equals(request.refreshToken())) {
            throw RefreshTokenMisMatchException.EXCEPTION;
        }

        User user = userRepository.findByName(name)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        Role role = user.getRole();

        String accessToken = jwtTokenProvider.generateAccessToken(name, role);
        String refreshToken = jwtTokenProvider.generateRefreshToken(name, role);

        return new TokenWithRoleResponse(accessToken, refreshToken, role);
    }
}
