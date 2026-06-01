package br.com.senai.projetointegrador.features.course.enrollStudent;

import br.com.senai.projetointegrador.features.course.Course;
import br.com.senai.projetointegrador.features.course.CourseDto;
import br.com.senai.projetointegrador.features.course.CourseRepository;
import br.com.senai.projetointegrador.features.course.exceptions.CourseNotFoundException;
import br.com.senai.projetointegrador.features.students.Student;
import br.com.senai.projetointegrador.features.students.StudentDto;
import br.com.senai.projetointegrador.features.students.StudentRepository;
import br.com.senai.projetointegrador.features.students.errors.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EnrollStudentHandler {
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;


    public EnrollStudentResponse handle(Long courseId, EnrollStudentRequest request) {
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        Student student = studentRepository
                .findById(request.studentId())
                .orElseThrow(() -> new StudentNotFoundException(request.studentId()));

        course.enrollStudent(student);
        courseRepository.save(course);

        // TODO: Create mappers for dtos.
        return new EnrollStudentResponse(
                new CourseDto(
                        course.getId(),
                        course.getName(),
                        course.getShift(),
                        course.getYear(),
                        course.getSemester()),
                new StudentDto(
                        student.getId(),
                        student.getName(),
                        student.getPicture(),
                        student.getBirthDate()
                ),
                LocalDateTime.now()
        );
    }
}
