package br.com.senai.projetointegrador.features.auth.register;

import br.com.senai.projetointegrador.features.users.User;
import br.com.senai.projetointegrador.features.users.UserRepository;
import br.com.senai.projetointegrador.security.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RegisterHandler {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    @Transactional
    public RegisterResponse handle(RegisterRequest request) {
        if (userRepository.existsByLogin(request.login())) {
            throw new InvalidLoginException();
        }

        var encodedPassword = passwordEncoder.encode(request.password());
        var user = new User(request.name(), request.login(), encodedPassword, request.role());
        var saved = userRepository.save(user);
        var token = jwtTokenService.generateTokenFor(saved);
        return new RegisterResponse(token);
    }
}
