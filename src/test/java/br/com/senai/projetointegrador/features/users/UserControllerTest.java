package br.com.senai.projetointegrador.features.users;

import br.com.senai.projetointegrador.IntegrationTestBase;
import br.com.senai.projetointegrador.features.users.getById.GetUserByIdRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest extends IntegrationTestBase {

    @Nested
    class GetById {

        @Nested
        class Validation {
            @Test
            void should_reject_request_with_no_id() {
                var request = new GetUserByIdRequest(null);
                var admin = userRepository.save(ADMIN);
                var token = getTokenFor(admin);
                client.get()
                      .uri("/users/{id}", request.id())
                      .headers(headers -> headers.setBearerAuth(token))
                      .exchange()
                      .expectStatus()
                      .isNotFound()
                      .expectBody(ProblemDetail.class);
            }

            @Test
            void should_return_user_with_valid_payload() {
                var teacher = userRepository.save(TEACHER);
                var admin = userRepository.save(ADMIN);

                var request = new GetUserByIdRequest(teacher.getId());
                var token = getTokenFor(admin);

                client.get()
                      .uri("/users/{id}", request.id())
                      .headers(headers -> headers.setBearerAuth(token))
                      .exchange()
                      .expectStatus()
                      .isOk()
                      .expectBody(UserDto.class)
                      .value(body -> {
                          assertThat(body).isNotNull();
                          assertThat(body.login()).isEqualTo(teacher.getLogin());
                          assertThat(body.role()).isEqualTo(teacher.getRole());
                      });
            }
        }

        @Nested
        class BussinessLogic {
            @Test
            void should_reject_unauthenticated_requests() {
                var teacher = userRepository.save(TEACHER);
                var request = new GetUserByIdRequest(teacher.getId());
                client.get()
                      .uri("/users/{id}", request.id())
                      .exchange()
                      .expectStatus()
                      .isEqualTo(HttpStatus.UNAUTHORIZED)
                      .expectBody(ProblemDetail.class);
            }

            @Test
            void should_allow_admin_requests() {
                var teacher = userRepository.save(TEACHER);
                var admin = userRepository.save(ADMIN);

                var request = new GetUserByIdRequest(teacher.getId());
                var token = getTokenFor(admin);

                client.get()
                      .uri("/users/{id}", request.id())
                      .headers(headers -> headers.setBearerAuth(token))
                      .exchange()
                      .expectStatus()
                      .isOk()
                      .expectBody(UserDto.class)
                      .value(body -> {
                          assertThat(body).isNotNull();
                          assertThat(body.login()).isEqualTo(teacher.getLogin());
                          assertThat(body.role()).isEqualTo(teacher.getRole());
                      });
            }

            @ParameterizedTest
            @EnumSource(value = Role.class, names = "ADMINISTRATIVE_TEACHER", mode = EnumSource.Mode.EXCLUDE)
            void should_reject_non_admin_requests(Role role) {
                var NON_ADMIN_USER = addUserToDb("name", "login", "rawPassword", role);
                var NON_ADMIN_TOKEN = getTokenFor(NON_ADMIN_USER);
                var request = new GetUserByIdRequest(NON_ADMIN_USER.getId());

                client.get()
                      .uri("/users/{id}", request.id())
                      .headers(headers -> headers.setBearerAuth(NON_ADMIN_TOKEN))
                      .exchange()
                      .expectStatus()
                      .isUnauthorized()
                      .expectBody(ProblemDetail.class)
                      .value(body -> {
                          assertThat(body).isNotNull();
                          assertThat(body.getDetail())
                                  .isEqualTo("Full authentication is required to access this resource");
                      });
            }
        }
    }
}
