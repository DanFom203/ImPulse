package ru.itis.impulse_back.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.impulse_back.security.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No JWT token found in request header for URI: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            DecodedJWT decodedToken = jwtService.parse(token);
            String email = decodedToken.getSubject();
            Instant tokenExpiresAt = decodedToken.getExpiresAtAsInstant();
            boolean isTokenExpired = tokenExpiresAt.isBefore(Instant.now());

            if (!isTokenExpired) {
                log.debug("Valid JWT for user [{}] on URI: {}", email, request.getRequestURI());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        email, null, Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("Expired JWT token for user [{}] on URI: {}", email, request.getRequestURI());
            }

            filterChain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            log.warn("JWT verification failed: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
