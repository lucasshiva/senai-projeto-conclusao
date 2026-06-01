package br.com.senai.projetointegrador.features.course;

public record CourseDto(
        Long id,
        String name,
        Shift shift,
        int year,
        Semester semester) {
}
