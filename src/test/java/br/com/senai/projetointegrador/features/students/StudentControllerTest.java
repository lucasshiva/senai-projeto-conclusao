package br.com.senai.projetointegrador.features.students;

import br.com.senai.projetointegrador.IntegrationTestBase;
import br.com.senai.projetointegrador.features.students.register.RegisterStudentRequest;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;

class StudentControllerTest extends IntegrationTestBase {

    @Nested
    class RegisterStudent {
        private final MockMultipartFile validPicture = new MockMultipartFile(
                "picture", "test.jpg", "image/jpeg", "content".getBytes());
        private final RegisterStudentRequest validRequest = new RegisterStudentRequest("John", LocalDate.now());

        private MultiValueMap<String, Object> partBuilderFor(RegisterStudentRequest request, @Nullable MockMultipartFile picture) {
            MultiValueMap<String, Object> builder = new LinkedMultiValueMap<>();
            builder.add("request", request);
            if (picture != null) {
                builder.add("picture", picture.getResource());
            }

            return builder;
        }

        private final RestTestClient.RequestBodySpec postSpecWithAuth =
                client.post()
                        .uri("/students")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .headers(h -> h.setBearerAuth(adminToken));

        @Nested
        class Validation {
            @ParameterizedTest
            @NullAndEmptySource
            void should_reject_missing_name(String name) {
                var request = validRequest.withName(name);
                var parts = partBuilderFor(request, validPicture);
                assertSingleValidationErrorFor("name", postSpecWithAuth.body(parts));
            }

            @Test
            void should_reject_missing_birthdate() {
                var request = validRequest.withBirthDate(null);
                var parts = partBuilderFor(request, validPicture);
                assertSingleValidationErrorFor("birthDate", postSpecWithAuth.body(parts));
            }

            @Nested
            class PictureValidation {
                @Test
                void should_reject_missing_picture() {
                    var parts = partBuilderFor(validRequest, null);
                    assertSingleValidationErrorFor("picture", postSpecWithAuth.body(parts));
                }

                @Test
                void should_reject_empty_picture() {
                    var emptyFile = new MockMultipartFile("picture", "empty.jpg", "image/jpeg", new byte[0]);
                    var parts = partBuilderFor(validRequest, emptyFile);
                    assertSingleValidationErrorFor("picture", postSpecWithAuth.body(parts));
                }

                @Test
                void should_reject_invalid_content_type() {
                    var scriptFile = new MockMultipartFile("picture", "test.sh", "application/x-sh", "content".getBytes());
                    var parts = partBuilderFor(validRequest, scriptFile);
                    assertSingleValidationErrorFor("picture", postSpecWithAuth.body(parts));
                }
            }
        }

        @Nested
        class BussinessRules {
            @Test
            void should_save_file_locally() {
                // Business logic test
            }
        }

    }
}