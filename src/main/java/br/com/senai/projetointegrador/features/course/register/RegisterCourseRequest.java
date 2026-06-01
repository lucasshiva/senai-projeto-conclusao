package br.com.senai.projetointegrador.features.course.register;

import br.com.senai.projetointegrador.features.course.Semester;
import br.com.senai.projetointegrador.features.course.Shift;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterCourseRequest(
        @NotBlank
        String name,

        @NotNull
        Shift shift,

        @NotNull
        int year,

        @NotNull
        Semester semester
) {
}
