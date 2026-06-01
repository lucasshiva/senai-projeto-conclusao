package br.com.senai.projetointegrador.features.course.register;

import br.com.senai.projetointegrador.features.course.Semester;
import br.com.senai.projetointegrador.features.course.Shift;

public record RegisterCourseResponse(
        Long id,
        String name,
        Shift shift,
        int year,
        Semester semester
) {
}
