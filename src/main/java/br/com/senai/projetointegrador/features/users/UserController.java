package br.com.senai.projetointegrador.features.users;

import br.com.senai.projetointegrador.security.JwtTokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final JwtTokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<@NonNull String> createUser() {
        var password = passwordEncoder.encode("123456");
        var user = new User("Lucas", "myusername", password, Role.ADMINISTRATIVE_TEACHER);
        userRepository.deleteAll();
        var saved = userRepository.save(user);
        IO.println("Created user: " + saved);
        var token = tokenService.generateTokenFor(saved);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull User> getUser(@PathVariable Long id) {
        var optUser = userRepository.findById(id);
        if (optUser.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(optUser.get());
    }
}
