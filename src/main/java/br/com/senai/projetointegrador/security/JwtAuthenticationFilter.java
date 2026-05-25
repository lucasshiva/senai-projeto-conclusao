package br.com.senai.projetointegrador.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtService;
    private final CustomAuthEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(
            UserDetailsService userDetailsService,
            JwtTokenService jwtService,
            CustomAuthEntryPoint authenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String tokenInHeader = getTokenFromRequest(request);
        if (tokenInHeader == null || tokenInHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        var existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (existingAuth != null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = jwtService.getSubjectFromToken(tokenInHeader);
            UserDetails user = userDetailsService.loadUserByUsername(username);
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            IO.println("Authentication set!");
        } catch (AuthenticationException | JWTVerificationException ex) {
            authenticationEntryPoint.commence(request, response, new BadCredentialsException(ex.getMessage(), ex));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null) return null;
        if (!header.startsWith("Bearer ")) return null;
        return header.replace("Bearer ", "");
    }
}
