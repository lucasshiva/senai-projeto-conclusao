package br.com.senai.projetointegrador.features.incidents.actions.getById;

import br.com.senai.projetointegrador.features.incidents.IncidentDto;
import br.com.senai.projetointegrador.features.incidents.IncidentMapper;
import br.com.senai.projetointegrador.features.incidents.IncidentRepository;
import br.com.senai.projetointegrador.features.incidents.exceptions.IncidentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetIncidentByIdHandler {
    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;

    public IncidentDto handle(GetIncidentByIdRequest request) {
        var incident = incidentRepository
                .findById(request.id())
                .orElseThrow(() -> new IncidentNotFoundException(request.id()));

        return incidentMapper.toDto(incident);
    }
}
