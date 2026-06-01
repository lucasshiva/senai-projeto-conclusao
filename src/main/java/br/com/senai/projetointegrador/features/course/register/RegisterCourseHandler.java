package br.com.senai.projetointegrador.features.course.register;

import br.com.senai.projetointegrador.features.course.Course;
import br.com.senai.projetointegrador.features.course.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RegisterCourseHandler {
    private final CourseRepository courseRepository;

    @Transactional
    public RegisterCourseResponse handle(RegisterCourseRequest request) {
        var course = new Course(request.name(), request.shift(), request.year(), request.semester());
        courseRepository.save(course);
        return new RegisterCourseResponse(
                course.getId(), course.getName(), course.getShift(), course.getYear(), course.getSemester());
    }
}
