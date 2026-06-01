package br.com.senai.projetointegrador.features.course;

import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    public CourseDto toDto(Course course) {
        return new CourseDto(
                course.getId(),
                course.getName(),
                course.getShift(),
                course.getYear(),
                course.getSemester()
        );
    }
}
