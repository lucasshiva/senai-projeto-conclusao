package br.com.senai.projetointegrador.features.incidents.actions.getById;

import jakarta.validation.constraints.NotNull;

public record GetIncidentByIdRequest(@NotNull Long id) {
}
