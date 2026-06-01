package br.com.senai.projetointegrador.features.incidents;

import br.com.senai.projetointegrador.features.incidents.actions.getById.GetIncidentByIdHandler;
import br.com.senai.projetointegrador.features.incidents.actions.getById.GetIncidentByIdRequest;
import br.com.senai.projetointegrador.features.incidents.actions.list.ListIncidentsHandler;
import br.com.senai.projetointegrador.features.incidents.actions.list.ListIncidentsRequest;
import br.com.senai.projetointegrador.features.incidents.actions.register.RegisterIncidentHandler;
import br.com.senai.projetointegrador.features.incidents.actions.register.RegisterIncidentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/incidents")
@RequiredArgsConstructor
public class IncidentController {
    private final RegisterIncidentHandler registerIncidentHandler;
    private final ListIncidentsHandler listIncidentsHandler;
    private final GetIncidentByIdHandler getIncidentByIdHandler;

    @PostMapping
    public ResponseEntity<IncidentDto> registerIncident(
            @RequestBody @Valid RegisterIncidentRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var response = registerIncidentHandler.handle(request);
        var uri = uriBuilder.path("/incidents/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<IncidentDto>> listIncidents(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) IncidentType type
    ) {
        var request = new ListIncidentsRequest(courseId, studentId, userId, type);
        var response = listIncidentsHandler.handle(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentDto> getIncidentById(@PathVariable Long id) {
        var request = new GetIncidentByIdRequest(id);
        var response = getIncidentByIdHandler.handle(request);
        return ResponseEntity.ok(response);
    }
}
