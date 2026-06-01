package br.com.senai.projetointegrador.features.incidents.actions.register;

import br.com.senai.projetointegrador.features.course.CourseRepository;
import br.com.senai.projetointegrador.features.course.exceptions.CourseNotFoundException;
import br.com.senai.projetointegrador.features.incidents.Incident;
import br.com.senai.projetointegrador.features.incidents.IncidentDto;
import br.com.senai.projetointegrador.features.incidents.IncidentMapper;
import br.com.senai.projetointegrador.features.incidents.IncidentRepository;
import br.com.senai.projetointegrador.features.students.StudentRepository;
import br.com.senai.projetointegrador.features.students.errors.StudentNotFoundException;
import br.com.senai.projetointegrador.features.users.UserNotFoundException;
import br.com.senai.projetointegrador.features.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RegisterIncidentHandler {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;

    @Transactional
    public IncidentDto handle(RegisterIncidentRequest request) {
        var course = courseRepository
                .findById(request.courseId())
                .orElseThrow(() -> new CourseNotFoundException(request.courseId()));

        var student = studentRepository
                .findById(request.studentId())
                .orElseThrow(() -> new StudentNotFoundException(request.studentId()));

        var user = userRepository
                .findById(request.userId())
                .orElseThrow(UserNotFoundException::new);

        var incident = new Incident(course, student, user, request.type(), request.description());
        incidentRepository.save(incident);
        return incidentMapper.toDto(incident);
    }
}
