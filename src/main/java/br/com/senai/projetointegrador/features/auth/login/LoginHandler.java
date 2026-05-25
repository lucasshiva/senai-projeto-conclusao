package br.com.senai.projetointegrador.features.auth.login;

import br.com.senai.projetointegrador.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginHandler {
    private final AuthenticationManager authManager;
    private final JwtTokenService jwtTokenService;

    public LoginResponse handle(LoginRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var auth = authManager.authenticate(authToken);
        if (auth.getPrincipal() == null) {
            throw new BadCredentialsException("Couldn't authenticate user");
        }
        var token = jwtTokenService.generateTokenFor((UserDetails) auth.getPrincipal());
        return new LoginResponse(token);
    }
}