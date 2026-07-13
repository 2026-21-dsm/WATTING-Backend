package com.whatting.global.security.jwt;

import com.whatting.domain.user.domain.RefreshToken;
import com.whatting.domain.user.domain.Role;
import com.whatting.domain.user.presentation.dto.response.TokenWithRoleResponse;
import com.whatting.domain.user.repository.RefreshTokenRepository;
import com.whatting.global.error.exception.CustomJwtException;
import com.whatting.global.security.auth.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperty jwtProperty;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenWithRoleResponse generateBothToken(String username, Role role) {
        String accessToken = generateAccessToken(username, role);
        String refreshToken = generateRefreshToken(username, role);

        return TokenWithRoleResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(role)
                .build();
    }

    public String generateAccessToken(String username, Role role) {
        return generateToken(username, role, "access", jwtProperty.getAccessExp());
    }

    public String generateRefreshToken(String username, Role role) {
        String refreshToken = generateToken(username, role, "refresh", jwtProperty.getRefreshExp());
        refreshTokenRepository.save(RefreshToken.builder()
                .username(username)
                .token(refreshToken)
                .ttl(jwtProperty.getRefreshExp())
                .build());

        return refreshToken;
    }

    private String generateToken(String username, Role role, String type, Long exp) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtProperty.getSecretKey())
                .setSubject(username)
                .setHeaderParam("type", type)
                .claim("authority", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + exp * 1000))
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerWithValue = request.getHeader(jwtProperty.getHeader());
        return parseToken(bearerWithValue);
    }

    private String parseToken(String bearerWithValue) {
        if(bearerWithValue != null && bearerWithValue.startsWith(jwtProperty.getPrefix())) {
            return bearerWithValue.replace(jwtProperty.getPrefix(), "");
        }
        return null;
    }

    public boolean validateToken(String token) { // TODO : 해당 예외 handler 처리
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtProperty.getSecretKey())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw new CustomJwtException.SignatureException();
        } catch (ExpiredJwtException e) {
            throw new CustomJwtException.ExpiredException();
        } catch (MalformedJwtException e) {
            throw new CustomJwtException.MalformedJwtException();
        } catch (IllegalArgumentException e) {
            throw new CustomJwtException.IllegalArgumentException();
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(getTokenBody(token).getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private Claims getTokenBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtProperty.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
