package br.com.senai.projetointegrador.features.incidents;

import org.springframework.data.jpa.domain.Specification;

public class IncidentSpecifications {
    public static Specification<Incident> hasStudent(Long studentId) {
        return (root, query, cb) -> cb.equal(root.get("student").get("id"), studentId);
    }

    public static Specification<Incident> hasCourse(Long courseId) {
        return (root, query, cb) -> cb.equal(root.get("course").get("id"), courseId);
    }

    public static Specification<Incident> hasUser(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Incident> hasType(IncidentType type) {
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }
}
