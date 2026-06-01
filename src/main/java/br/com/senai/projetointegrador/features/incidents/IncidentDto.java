package br.com.senai.projetointegrador.features.incidents;

import br.com.senai.projetointegrador.features.course.CourseDto;
import br.com.senai.projetointegrador.features.students.StudentDto;
import br.com.senai.projetointegrador.features.users.UserDto;

import java.time.LocalDateTime;

public record IncidentDto(
        Long id,
        CourseDto course,
        StudentDto student,
        UserDto user,
        IncidentType type,
        String description,
        LocalDateTime createdAt
) {
}
