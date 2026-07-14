package com.whatting.global.config;

import com.whatting.global.error.ErrorResponse;
import com.whatting.global.security.jwt.JwtAuthenticationFilter;
import com.whatting.global.security.jwt.JwtProperty;
import com.whatting.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperty.class)
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다"))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다"))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/teachers/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/alerts").hasAuthority("TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/alerts/active").hasAnyAuthority("STUDENT", "TEACHER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/alerts/{alertId}/type").hasAuthority("TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/alerts/{alertId}/close").hasAuthority("TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/alerts/{alertId}/me").hasAuthority("STUDENT")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/alerts/{alertId}/me/status").hasAuthority("STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/v1/alerts/{alertId}/help-requests").hasAuthority("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/v1/alerts/{alertId}/help-requests/me").hasAuthority("STUDENT")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/alerts/{alertId}/help-requests/me").hasAuthority("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/v1/alerts/{alertId}/help-requests").hasAuthority("TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/alerts/{alertId}/help-requests/{helpRequestId}").hasAuthority("TEACHER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/alerts/{alertId}/help-requests/status").hasAuthority("TEACHER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/alerts/{alertId}/help-requests/{helpRequestId}/status").hasAuthority("TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/alerts/{alertId}/students").hasAuthority("TEACHER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/alerts/{alertId}/students/{studentId}/confirmation").hasAuthority("TEACHER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, objectMapper),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void writeErrorResponse(HttpServletResponse response, int status, String message) throws java.io.IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(status, message)));
    }
}
