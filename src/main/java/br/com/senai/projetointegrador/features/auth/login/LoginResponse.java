package br.com.senai.projetointegrador.features.auth.login;

import jakarta.validation.constraints.NotBlank;

public record LoginResponse(@NotBlank String token) {}
