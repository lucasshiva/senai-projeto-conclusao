package br.com.senai.projetointegrador.features.auth.login;

import jakarta.validation.constraints.NotBlank;
import lombok.With;

@With
public record LoginRequest(
        @NotBlank String username, @NotBlank String password) {}
