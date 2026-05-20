package br.com.senai.projetointegrador.features.auth.register;

import jakarta.validation.constraints.NotBlank;

public record RegisterResponse(@NotBlank String token) {}
