package br.com.senai.projetointegrador.features.users;

import br.com.senai.projetointegrador.features.users.getById.GetUserByIdHandler;
import br.com.senai.projetointegrador.features.users.getById.GetUserByIdRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final GetUserByIdHandler getUserByIdHandler;

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull UserDto> getById(@PathVariable Long id) {
        var response = getUserByIdHandler.handle(new GetUserByIdRequest(id));
        return ResponseEntity.ok(response);
    }
}
