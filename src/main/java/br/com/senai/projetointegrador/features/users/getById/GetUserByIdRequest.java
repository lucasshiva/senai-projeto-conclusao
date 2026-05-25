package br.com.senai.projetointegrador.features.users.getById;

import jakarta.validation.constraints.NotNull;

public record GetUserByIdRequest(@NotNull Long id) {}
