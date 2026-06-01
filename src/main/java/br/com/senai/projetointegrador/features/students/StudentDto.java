package br.com.senai.projetointegrador.features.students;

import java.time.LocalDate;

public record StudentDto(
        Long id,
        String name,
        String picture,
        LocalDate birthDate) {
}
