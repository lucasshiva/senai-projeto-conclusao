package br.com.senai.projetointegrador.features.auth.register;

import br.com.senai.projetointegrador.features.users.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.With;

@With
public record RegisterRequest(
        @NotBlank String name,
        @NotBlank String login,
        @NotBlank String password,
        @NotNull Role role) {}
