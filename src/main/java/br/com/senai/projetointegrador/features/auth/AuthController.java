package br.com.senai.projetointegrador.features.auth;

import br.com.senai.projetointegrador.features.auth.login.LoginHandler;
import br.com.senai.projetointegrador.features.auth.login.LoginRequest;
import br.com.senai.projetointegrador.features.auth.login.LoginResponse;
import br.com.senai.projetointegrador.features.auth.register.RegisterHandler;
import br.com.senai.projetointegrador.features.auth.register.RegisterRequest;
import br.com.senai.projetointegrador.features.auth.register.RegisterResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginHandler loginHandler;
    private final RegisterHandler registerHandler;

    @PostMapping("/login")
    public ResponseEntity<@NonNull LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        var response = loginHandler.handle(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<@NonNull RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        var response = registerHandler.handle(request);
        return ResponseEntity.ok(response);
    }
}
