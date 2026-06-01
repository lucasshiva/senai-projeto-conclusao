package br.com.senai.projetointegrador.features.incidents.actions.register;

import br.com.senai.projetointegrador.features.incidents.IncidentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterIncidentRequest(
        @NotNull Long courseId,
        @NotNull Long studentId,
        @NotNull Long userId,
        @NotNull IncidentType type,
        @NotBlank String description
) {
}
