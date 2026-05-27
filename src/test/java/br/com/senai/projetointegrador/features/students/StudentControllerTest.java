package br.com.senai.projetointegrador.features.students;

import br.com.senai.projetointegrador.IntegrationTestBase;
import br.com.senai.projetointegrador.config.storage.StorageProperties;
import br.com.senai.projetointegrador.features.students.register.RegisterStudentRequest;
import br.com.senai.projetointegrador.features.students.register.RegisterStudentResponse;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StudentControllerTest extends IntegrationTestBase {

    @Autowired
    private StorageProperties storageProperties;

    private void deleteDirectory(Path path) throws IOException {
        if (!Files.exists(path)) return;

        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }

    @BeforeEach
    void tearDown() throws IOException {
        deleteDirectory(storageProperties.studentPictures());
    }

    @Nested
    class RegisterStudent {
        private final MockMultipartFile validPicture =
                new MockMultipartFile("picture", "test.jpg", "image/jpeg", "content".getBytes());
        private final RegisterStudentRequest validRequest = new RegisterStudentRequest("John", LocalDate.now());

        private MultiValueMap<String, Object> partsFor(
                RegisterStudentRequest request, @Nullable MockMultipartFile picture) {
            MultiValueMap<String, Object> builder = new LinkedMultiValueMap<>();
            builder.add("request", request);
            if (picture != null) {
                builder.add("picture", picture.getResource());
            }

            return builder;
        }

        private final RestTestClient.RequestBodySpec postSpec =
                client.post()
                      .uri("/students")
                      .contentType(MediaType.MULTIPART_FORM_DATA);

        private RestTestClient.RequestBodySpec postSpecWithAdminAuth() {
            var admin = userRepository.save(ADMIN);
            var adminToken = getTokenFor(admin);
            return postSpec.headers(h -> h.setBearerAuth(adminToken));
        }

        @Nested
        class Validation {
            @ParameterizedTest
            @NullAndEmptySource
            void should_reject_missing_name(String name) {
                var request = validRequest.withName(name);
                var parts = partsFor(request, validPicture);

                assertSingleValidationErrorFor("name", postSpecWithAdminAuth().body(parts));
            }

            @Test
            void should_reject_missing_birthdate() {
                var request = validRequest.withBirthDate(null);
                var parts = partsFor(request, validPicture);
                assertSingleValidationErrorFor("birthDate", postSpecWithAdminAuth().body(parts));
            }

            @Nested
            class PictureValidation {
                @Test
                void should_reject_missing_picture() {
                    var parts = partsFor(validRequest, null);
                    postSpecWithAdminAuth().body(parts).exchange().expectStatus().isBadRequest();
                }

                @Test
                void should_reject_empty_picture() {
                    var emptyFile = new MockMultipartFile("picture", "empty.jpg", "image/jpeg", new byte[0]);
                    var parts = partsFor(validRequest, emptyFile);
                    postSpecWithAdminAuth().body(parts).exchange().expectStatus().isBadRequest();
                }

                @Test
                void should_reject_invalid_content_type() {
                    var scriptFile =
                            new MockMultipartFile("picture", "test.sh", "application/x-sh", "content".getBytes());
                    var parts = partsFor(validRequest, scriptFile);
                    postSpecWithAdminAuth().body(parts).exchange().expectStatus().isBadRequest();
                }
            }
        }

        @Nested
        class Auth {
            @Test
            void should_accept_admin_requests() {
                var parts = partsFor(validRequest, validPicture);
                var admin = userRepository.save(ADMIN);
                var adminToken = getTokenFor(admin);
                postSpec
                        .body(parts)
                        .headers(h -> h.setBearerAuth(adminToken))
                        .exchange().expectStatus().isOk();
            }

            @Test
            void should_reject_teacher_requests() {
                var parts = partsFor(validRequest, validPicture);
                var teacher = userRepository.save(TEACHER);
                var token = getTokenFor(teacher);

                postSpec.headers(h -> h.setBearerAuth(token))
                        .body(parts)
                        .exchange()
                        .expectStatus()
                        .isUnauthorized();
            }

            @Test
            void should_reject_qa_requests() {
                var parts = partsFor(validRequest, validPicture);
                var qa = userRepository.save(QUALITY_ANALYST);
                var token = getTokenFor(qa);

                postSpec.headers(h -> h.setBearerAuth(token))
                        .body(parts)
                        .exchange()
                        .expectStatus()
                        .isUnauthorized();
            }

            @Test
            void should_reject_coordinator_requests() {
                var parts = partsFor(validRequest, validPicture);
                var coordinator = userRepository.save(COORDINATOR);
                var token = getTokenFor(coordinator);

                postSpec.headers(h -> h.setBearerAuth(token))
                        .body(parts)
                        .exchange()
                        .expectStatus()
                        .isUnauthorized();
            }
        }

        @Nested
        class BussinessRules {
            @Test
            void should_save_file_locally() {
                var parts = partsFor(validRequest, validPicture);
                var admin = userRepository.save(ADMIN);
                var adminToken = getTokenFor(admin);

                postSpec
                        .body(parts)
                        .headers(h -> h.setBearerAuth(adminToken))
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody(RegisterStudentResponse.class)
                        .value(body -> {
                            assertThat(body).isNotNull();
                            var picturePath = Path.of(body.picture());
                            assertThat(picturePath).exists();
                            assertThat(picturePath.toFile().length()).isGreaterThan(0);
                        });
            }
        }
    }
}
