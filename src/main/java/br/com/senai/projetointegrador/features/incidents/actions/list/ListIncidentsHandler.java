package br.com.senai.projetointegrador.features.incidents.actions.list;

import br.com.senai.projetointegrador.features.incidents.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListIncidentsHandler {
    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;

    public List<IncidentDto> handle(ListIncidentsRequest request) {
        Specification<Incident> spec = Specification.unrestricted();

        if (request.userId() != null) {
            spec = spec.and(IncidentSpecifications.hasUser(request.userId()));
        }

        if (request.studentId() != null) {
            spec = spec.and(IncidentSpecifications.hasStudent(request.studentId()));
        }

        if (request.courseId() != null) {
            spec = spec.and(IncidentSpecifications.hasCourse(request.courseId()));
        }

        if (request.type() != null) {
            spec = spec.and(IncidentSpecifications.hasType(request.type()));
        }

        var incidents = incidentRepository.findAll(spec);
        return incidents.stream().map(incidentMapper::toDto).toList();
    }
}
