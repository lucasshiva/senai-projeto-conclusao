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
                client.get()
                        .uri("/users/{id}", request.id())
                        .headers(headers -> headers.setBearerAuth(adminToken))
                        .exchange()
                        .expectStatus()
                        .isNotFound()
                        .expectBody(ProblemDetail.class);
            }

            @Test
            void should_return_user_with_valid_payload() {
                var user = addUserToDb("validLogin", "validPassword");
                var request = new GetUserByIdRequest(user.getId());
                client.get()
                        .uri("/users/{id}", request.id())
                        .headers(headers -> headers.setBearerAuth(adminToken))
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(UserDto.class)
                        .value(body -> {
                            assertThat(body).isNotNull();
                            assertThat(body.login()).isEqualTo(user.getLogin());
                            assertThat(body.role()).isEqualTo(user.getRole());
                        });
            }
        }

        @Nested
        class BussinessLogic {
            @Test
            void should_reject_unauthenticated_requests() {
                var user = addUserToDb("normalLogin", "normalPass");
                var request = new GetUserByIdRequest(user.getId());
                client.get()
                        .uri("/users/{id}", request.id())
                        .exchange()
                        .expectStatus()
                        .isEqualTo(HttpStatus.UNAUTHORIZED)
                        .expectBody(ProblemDetail.class);
            }

            @Test
            void should_allow_admin_requests() {
                var user = addUserToDb("adminLogin", "adminPass");
                var request = new GetUserByIdRequest(user.getId());
                client.get()
                        .uri("/users/{id}", request.id())
                        .headers(headers -> headers.setBearerAuth(adminToken))
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(UserDto.class)
                        .value(body -> {
                            assertThat(body).isNotNull();
                            assertThat(body.login()).isEqualTo(user.getLogin());
                            assertThat(body.role()).isEqualTo(user.getRole());
                        });
            }

            @ParameterizedTest
            @EnumSource(value = Role.class, names = "ADMINISTRATIVE_TEACHER", mode = EnumSource.Mode.EXCLUDE)
            void should_reject_non_admin_requests(Role role) {
                var rawPassword = "rawPassword";
                var user = addUserToDb("login", rawPassword, role);
                var request = new GetUserByIdRequest(user.getId());
                var token = getTokenFor(user);
                client.get()
                        .uri("/users/{id}", request.id())
                        .headers(headers -> headers.setBearerAuth(token))
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
