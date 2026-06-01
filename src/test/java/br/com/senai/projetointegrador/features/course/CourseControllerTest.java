package br.com.senai.projetointegrador.features.course;

import br.com.senai.projetointegrador.IntegrationTestBase;
import br.com.senai.projetointegrador.features.course.register.RegisterCourseRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CourseControllerTest extends IntegrationTestBase {
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
                  .isCreated();
        }
    }
}