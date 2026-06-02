package br.com.senai.projetointegrador.features.incidents;

import br.com.senai.projetointegrador.IntegrationTestBase;
import br.com.senai.projetointegrador.features.course.Course;
import br.com.senai.projetointegrador.features.course.CourseRepository;
import br.com.senai.projetointegrador.features.course.Semester;
import br.com.senai.projetointegrador.features.course.Shift;
import br.com.senai.projetointegrador.features.incidents.actions.register.RegisterIncidentRequest;
import br.com.senai.projetointegrador.features.students.Student;
import br.com.senai.projetointegrador.features.students.StudentRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ProblemDetail;

import java.time.LocalDate;

class IncidentControllerTest extends IntegrationTestBase {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    private final Course validCourse = new Course("Java", Shift.AFTERNOON, 2025, Semester.SECOND);
    private final Student validStudent = new Student("John", "photos", LocalDate.now());

    @Nested
    class RegisterIncident {
        @Test
        void should_register_incident_with_valid_payload() {
            var user = userRepository.save(TEACHER);
            var authToken = getTokenFor(user);

            var course = courseRepository.save(validCourse);
            var student = studentRepository.save(validStudent);

            var request = new RegisterIncidentRequest(
                    course.getId(),
                    student.getId(),
                    user.getId(),
                    IncidentType.DISCIPLINARY,
                    "Random description"
            );

            client.post()
                  .uri("/incidents")
                  .body(request)
                  .headers(h -> h.setBearerAuth(authToken))
                  .exchange()
                  .expectStatus()
                  .isCreated()
                  .expectBody(IncidentDto.class);
        }

        @Test
        void should_return_not_found_if_user_does_not_exist() {
            var user = userRepository.save(TEACHER);
            var authToken = getTokenFor(user);
            var invalidUserId = 2L;

            var course = courseRepository.save(validCourse);
            var student = studentRepository.save(validStudent);

            var request = new RegisterIncidentRequest(
                    course.getId(),
                    student.getId(),
                    invalidUserId,
                    IncidentType.DISCIPLINARY,
                    "Random description"
            );

            client.post()
                  .uri("/incidents")
                  .headers(h -> h.setBearerAuth(authToken))
                  .body(request)
                  .exchange()
                  .expectStatus()
                  .isNotFound()
                  .expectBody(ProblemDetail.class);
        }

        @Test
        void should_return_not_found_if_student_does_not_exist() {
            var user = userRepository.save(TEACHER);
            var authToken = getTokenFor(user);
            var invalidStudentId = 1L;

            var course = courseRepository.save(validCourse);

            var request = new RegisterIncidentRequest(
                    course.getId(),
                    invalidStudentId,
                    user.getId(),
                    IncidentType.DISCIPLINARY,
                    "Random description"
            );

            client.post()
                  .uri("/incidents")
                  .headers(h -> h.setBearerAuth(authToken))
                  .body(request)
                  .exchange()
                  .expectStatus()
                  .isNotFound()
                  .expectBody(ProblemDetail.class);
        }

        @Test
        void should_return_not_found_if_course_does_not_exist() {
            var user = userRepository.save(TEACHER);
            var authToken = getTokenFor(user);
            var invalidCourseId = 1L;

            var student = studentRepository.save(validStudent);

            var request = new RegisterIncidentRequest(
                    invalidCourseId,
                    student.getId(),
                    user.getId(),
                    IncidentType.DISCIPLINARY,
                    "Random description"
            );

            client.post()
                  .uri("/incidents")
                  .headers(h -> h.setBearerAuth(authToken))
                  .body(request)
                  .exchange()
                  .expectStatus()
                  .isNotFound()
                  .expectBody(ProblemDetail.class);
        }
    }

    @Nested
    class GetIncidentById {
        @Test
        void should_get_incident_by_id() {
            var user = userRepository.save(COORDINATOR);
            var authToken = getTokenFor(user);

            var course = courseRepository.save(validCourse);
            var student = studentRepository.save(validStudent);
            var tempIncident = new Incident(course, student, user, IncidentType.ADMINISTRATIVE, "A description");
            var incident = incidentRepository.save(tempIncident);

            client.get()
                  .uri("/incidents/{id}", incident.getId())
                  .headers(h -> h.setBearerAuth(authToken))
                  .exchange()
                  .expectStatus()
                  .isOk()
                  .expectBody(IncidentDto.class);
        }

        @Test
        void should_return_not_found_when_incident_does_not_exist() {
            var user = userRepository.save(COORDINATOR);
            var authToken = getTokenFor(user);
            
            client.get()
                  .uri("/incidents/{id}", 1L)
                  .headers(h -> h.setBearerAuth(authToken))
                  .exchange()
                  .expectStatus()
                  .isNotFound()
                  .expectBody(ProblemDetail.class);
        }
    }
}