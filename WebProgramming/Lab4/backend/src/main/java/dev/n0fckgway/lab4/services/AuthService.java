package dev.n0fckgway.lab4.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.security.enterprise.AuthenticationException;


@Stateless
public class AuthService {

    @EJB
    private JwtService jwtService;

    private String normalizeToken(String token) throws AuthenticationException {
        if (token == null || token.isBlank()) {
            throw new AuthenticationException("Авторизационный токен необходим!");

        }

        String newToken = token.trim();

        if (newToken.startsWith("Bearer ")) {
            return newToken.substring("Bearer ".length());
        }
        return newToken;

    }

    public Long getUserIdByToken(String token) throws AuthenticationException {
        String newToken = normalizeToken(token);

        if (!jwtService.checkValidToken(newToken)) {
            throw new AuthenticationException("Невалидный токен!");
        }
        return jwtService.extractUserId(newToken);
    }


}
