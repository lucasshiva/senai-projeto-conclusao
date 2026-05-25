package br.com.senai.projetointegrador.features.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDto(
        @NotBlank String name,
        @NotBlank String login,
        @NotNull Role role) {}
