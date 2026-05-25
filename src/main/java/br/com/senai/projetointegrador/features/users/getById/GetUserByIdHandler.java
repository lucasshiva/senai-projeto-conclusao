package br.com.senai.projetointegrador.features.users.getById;

import br.com.senai.projetointegrador.features.users.UserDto;
import br.com.senai.projetointegrador.features.users.UserNotFoundException;
import br.com.senai.projetointegrador.features.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserByIdHandler {
    private final UserRepository userRepository;

    public UserDto handle(GetUserByIdRequest request) {
        var user = userRepository.findById(request.id()).orElseThrow(UserNotFoundException::new);
        return new UserDto(user.getName(), user.getLogin(), user.getRole());
    }
}
