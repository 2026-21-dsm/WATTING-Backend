package com.whatting.domain.user.presentation;

import com.whatting.domain.user.presentation.dto.request.AuthRequest;
import com.whatting.domain.user.presentation.dto.request.RefreshTokenRequest;
import com.whatting.domain.user.presentation.dto.request.StudentSignupRequest;
import com.whatting.domain.user.presentation.dto.request.TeacherSignupRequest;
import com.whatting.domain.user.presentation.dto.response.TokenWithRoleResponse;
import com.whatting.domain.user.presentation.dto.response.UserResponse;
import com.whatting.domain.user.service.LoginService;
import com.whatting.domain.user.service.SignupService;
import com.whatting.domain.user.service.TokenRefreshService;
import com.whatting.global.security.auth.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final SignupService signupService;
    private final LoginService loginService;
    private final TokenRefreshService tokenRefreshService;

    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signupStudent(@Valid @RequestBody StudentSignupRequest request) {
        return signupService.signupStudent(request);
    }

    @PostMapping("/auth/teachers/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signupTeacher(@Valid @RequestBody TeacherSignupRequest request) {
        return signupService.signupTeacher(request);
    }

    @PostMapping("/auth/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenWithRoleResponse login(@Valid @RequestBody AuthRequest authRequest) {
        return loginService.execute(authRequest);
    }

    @PostMapping("/auth/refresh")
    @ResponseStatus(HttpStatus.OK)
    public TokenWithRoleResponse refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return tokenRefreshService.execute(refreshTokenRequest);
    }

    @GetMapping("/users/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse me(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return UserResponse.from(userDetails.getUser());
    }
}
