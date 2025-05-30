package ru.itis.impulse_back.security.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.security.service.JWTService;

@Slf4j
@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {

    private final UserRepository userRepository;

    @Value("${jwt.lifetime}")
    private Duration tokenLifetime;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String generateToken(User user) {
        String email = user.getEmail();
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(tokenLifetime);
        log.info("Generating JWT for user [{}], valid until {}", email, expiresAt);

        return JWT.create()
                .withSubject(email)
                .withIssuedAt(Date.from(issuedAt))
                .withExpiresAt(Date.from(expiresAt))
                .withClaim("id", userRepository.findByEmail(email).get().getId())
                .sign(Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public DecodedJWT parse(String token) throws JWTVerificationException {
        log.debug("Parsing JWT token");
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    @Override
    public Map<String, Claim> getClaims(String token) {
        log.debug("Extracting claims from JWT");
        DecodedJWT decodedJWT = parse(token);
        return decodedJWT.getClaims();
    }
}
