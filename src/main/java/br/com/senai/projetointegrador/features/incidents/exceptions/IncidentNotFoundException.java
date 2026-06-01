package br.com.senai.projetointegrador.features.incidents.exceptions;

public class IncidentNotFoundException extends RuntimeException {
    public IncidentNotFoundException(Long incidentId) {
        super("Incident with ID " + incidentId + " not found");
    }
}
