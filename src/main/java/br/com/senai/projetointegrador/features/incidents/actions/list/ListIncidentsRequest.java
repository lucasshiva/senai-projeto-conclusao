package br.com.senai.projetointegrador.features.incidents.actions.list;

import br.com.senai.projetointegrador.features.incidents.IncidentType;

public record ListIncidentsRequest(
        Long courseId,
        Long studentId,
        Long userId,
        IncidentType type
) {
}
