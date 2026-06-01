package br.com.senai.projetointegrador.features.course.enrollStudent;

import br.com.senai.projetointegrador.features.course.CourseDto;
import br.com.senai.projetointegrador.features.students.StudentDto;

import java.time.LocalDateTime;

public record EnrollStudentResponse(CourseDto course, StudentDto student, LocalDateTime enrollDate) {
}
