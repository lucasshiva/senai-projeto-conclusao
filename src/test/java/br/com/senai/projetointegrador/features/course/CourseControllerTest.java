package br.com.senai.projetointegrador.features.course;

import br.com.senai.projetointegrador.IntegrationTestBase;
import br.com.senai.projetointegrador.features.course.enrollStudent.EnrollStudentRequest;
import br.com.senai.projetointegrador.features.course.enrollStudent.EnrollStudentResponse;
import br.com.senai.projetointegrador.features.course.register.RegisterCourseRequest;
import br.com.senai.projetointegrador.features.course.register.RegisterCourseResponse;
import br.com.senai.projetointegrador.features.students.Student;
import br.com.senai.projetointegrador.features.students.StudentRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

class CourseControllerTest extends IntegrationTestBase {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Nested
    class RegisterCourse {
        RegisterCourseRequest validRequest = new RegisterCourseRequest(
                "Java - Spring Boot", Shift.EVENING, 2026, Semester.FIRST);

        @Test
        void shouldRegisterCourse() {
            var admin = userRepository.save(ADMIN);
            var adminToken = getTokenFor(admin);
            client.post()
                  .uri("/courses")
                  .body(validRequest)
                  .headers(h -> h.setBearerAuth(adminToken))
                  .exchange()
                  .expectStatus()
                  .isCreated()
                  .expectBody(RegisterCourseResponse.class);
        }
    }

    @Nested
    class EnrollStudent {
        Student validStudent = new Student("John", "uploads/john.png", LocalDate.of(2000, 5, 12));
        Course validCourse = new Course("Spring Boot", Shift.EVENING, 2026, Semester.FIRST);

        @Test
        void shouldEnrollStudent() {
            var admin = userRepository.save(ADMIN);
            var adminToken = getTokenFor(admin);

            studentRepository.save(validStudent);
            courseRepository.save(validCourse);

            var request = new EnrollStudentRequest(validStudent.getId());
            client.post()
                  .uri("/courses/{id}/students", validCourse.getId())
                  .body(request)
                  .headers(h -> h.setBearerAuth(adminToken))
                  .exchange()
                  .expectStatus()
                  .isOk()
                  .expectBody(EnrollStudentResponse.class);

        }
    }
}