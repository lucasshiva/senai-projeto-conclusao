package br.com.senai.projetointegrador.features.auth;

import br.com.senai.projetointegrador.IntegrationTestBase;
import br.com.senai.projetointegrador.features.auth.login.LoginRequest;
import br.com.senai.projetointegrador.features.auth.login.LoginResponse;
import br.com.senai.projetointegrador.features.auth.register.RegisterRequest;
import br.com.senai.projetointegrador.features.auth.register.RegisterResponse;
import br.com.senai.projetointegrador.features.users.Role;
import br.com.senai.projetointegrador.features.users.User;
import br.com.senai.projetointegrador.features.users.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

class AuthControllerTest extends IntegrationTestBase {

    @Autowired
    private RestTestClient client;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String validLogin = "lucas";
    private final String validPassword = "mypassword";

    @Nested
    class Register {

        private final RegisterRequest validRequest =
                new RegisterRequest("lucas", validLogin, validPassword, Role.TEACHER);

        private void registerWithSuccessSuccess(RegisterRequest request) {
            client.post()
                    .uri("/auth/register")
                    .body(request)
                    .exchange()
                    .expectStatus()
                    .isOk();
        }

        private void registerWithValidationFailure(RegisterRequest request) {
            client.post()
                    .uri("/auth/register")
                    .body(request)
                    .exchange()
                    .expectStatus()
                    .isBadRequest();
        }

        @Nested
        class Validation {
            @Test
            void should_register_with_valid_payload() {
                registerWithSuccessSuccess(validRequest);
            }

            @ParameterizedTest
            @NullAndEmptySource
            void should_reject_blank_or_missing_name(String name) {
                var request = validRequest.withName(name);
                registerWithValidationFailure(request);
            }

            @ParameterizedTest
            @NullAndEmptySource
            void should_reject_blank_or_missing_login(String login) {
                var request = validRequest.withLogin(login);
                registerWithValidationFailure(request);
            }

            @ParameterizedTest
            @NullAndEmptySource
            void should_reject_blank_or_missing_password(String password) {
                var request = validRequest.withPassword(password);
                registerWithValidationFailure(request);
            }

            @Test
            void should_reject_missing_role() {
                var request = validRequest.withRole(null);
                registerWithValidationFailure(request);
            }
        }

        @Nested
        class BusinessLogic {

            @Test
            void should_encode_password_on_register() {
                registerWithSuccessSuccess(validRequest);
                var optUser = userRepository.findByLogin(validRequest.login());
                assertThat(optUser).isPresent();

                var user = optUser.get();
                var wasPasswordEncoded = passwordEncoder.matches(validRequest.password(), user.getPassword());
                assertThat(wasPasswordEncoded).isTrue();
            }

            @Test
            void should_return_token_on_register() {
                client.post()
                        .uri("/auth/register")
                        .body(validRequest)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(RegisterResponse.class)
                        .value(body -> {
                            assertThat(body).isNotNull();
                            assertThat(body.token()).isNotNull();
                            assertThat(body.token()).isNotBlank();
                        });
            }
        }
    }

    @Nested
    class Login {
        private final LoginRequest validRequest = new LoginRequest(validLogin, validPassword);

        private User addTestUserToDb(String login, String rawPassword) {
            var password = passwordEncoder.encode(rawPassword);
            var user = new User("test", login, password, Role.TEACHER);
            return userRepository.save(user);
        }

        @Nested
        class Validation {
            @Test
            void should_login_with_valid_payload() {
                addTestUserToDb(validRequest.username(), validRequest.password());
                client.post()
                        .uri("/auth/login")
                        .body(validRequest)
                        .exchange()
                        .expectStatus()
                        .isOk();
            }

            @ParameterizedTest
            @NullAndEmptySource
            void should_reject_blank_or_missing_username(String username) {
                var request = validRequest.withUsername(username);
                client.post()
                        .uri("/auth/login")
                        .body(request)
                        .exchange()
                        .expectStatus()
                        .isBadRequest();
            }

            @ParameterizedTest
            @NullAndEmptySource
            void should_reject_blank_or_missing_password(String password) {
                var request = validRequest.withPassword(password);
                client.post()
                        .uri("/auth/login")
                        .body(request)
                        .exchange()
                        .expectStatus()
                        .isBadRequest();
            }
        }

        @Nested
        class BusinessLogic {
            @Test
            void should_return_token_on_login() {
                addTestUserToDb(validRequest.username(), validRequest.password());
                client.post()
                        .uri("/auth/login")
                        .body(validRequest)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(LoginResponse.class)
                        .value(body -> {
                            assertThat(body).isNotNull();
                            assertThat(body.token()).isNotNull();
                            assertThat(body.token()).isNotBlank();
                        });
            }
        }
    }
}
