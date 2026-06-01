package br.com.senai.projetointegrador.features.course.enrollStudent;

import jakarta.validation.constraints.NotNull;

public record EnrollStudentRequest(@NotNull Long studentId) {
}
