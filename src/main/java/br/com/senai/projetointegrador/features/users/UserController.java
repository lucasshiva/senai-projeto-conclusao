package br.com.senai.projetointegrador.features.users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> createUser() {
        var user = new User("Lucas", "teste", "teste", Role.ADMINISTRATIVE_TEACHER);
        var saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        var optUser = userRepository.findById(id);
        if (optUser.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(optUser.get());
    }
}
