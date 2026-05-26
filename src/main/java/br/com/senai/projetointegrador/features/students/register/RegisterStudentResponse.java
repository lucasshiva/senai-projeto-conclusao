package br.com.senai.projetointegrador.features.students.register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterStudentResponse(
        @NotNull Long id,
        @NotBlank String name,
        @NotBlank String picture,
        @NotNull LocalDate birthDate) {}
