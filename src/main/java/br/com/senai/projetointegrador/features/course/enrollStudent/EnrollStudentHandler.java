package br.com.senai.projetointegrador.features.course.enrollStudent;

import br.com.senai.projetointegrador.features.course.Course;
import br.com.senai.projetointegrador.features.course.CourseMapper;
import br.com.senai.projetointegrador.features.course.CourseRepository;
import br.com.senai.projetointegrador.features.course.exceptions.CourseNotFoundException;
import br.com.senai.projetointegrador.features.students.Student;
import br.com.senai.projetointegrador.features.students.StudentMapper;
import br.com.senai.projetointegrador.features.students.StudentRepository;
import br.com.senai.projetointegrador.features.students.errors.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EnrollStudentHandler {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;


    @Transactional
    public EnrollStudentResponse handle(Long courseId, EnrollStudentRequest request) {
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        Student student = studentRepository
                .findById(request.studentId())
                .orElseThrow(() -> new StudentNotFoundException(request.studentId()));

        course.enrollStudent(student);
        courseRepository.save(course);

        return new EnrollStudentResponse(
                courseMapper.toDto(course),
                studentMapper.toDto(student),
                LocalDateTime.now()
        );
    }
}
