package br.com.senai.projetointegrador.features.users;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        return new UserDto(user.getName(), user.getLogin(), user.getRole());
    }
}
