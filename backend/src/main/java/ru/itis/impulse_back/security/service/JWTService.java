package ru.itis.impulse_back.security.service;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import ru.itis.impulse_back.model.User;

import java.util.Map;

public interface JWTService {
    String generateToken(User user);

    DecodedJWT parse(String token) throws JWTVerificationException;

    Map<String, Claim> getClaims(String token);
}
