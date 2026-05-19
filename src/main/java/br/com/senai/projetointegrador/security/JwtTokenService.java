package br.com.senai.projetointegrador.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtTokenService {
    private final String issuer = "ProjetoIntegradorSenai";
    private final Algorithm algorithm;

    public JwtTokenService(@Value("${api.security.token.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateTokenFor(UserDetails user) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getUsername())
                .withExpiresAt(LocalDateTime.now().plusHours(6).toInstant(ZoneOffset.of("-03:00")))
                .sign(algorithm);
    }

    public String getSubjectFromToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build();
        return verifier.verify(token).getSubject();
    }
}
