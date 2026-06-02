package br.com.senai.projetointegrador;

import br.com.senai.projetointegrador.errors.FieldErrorDetail;
import br.com.senai.projetointegrador.features.users.Role;
import br.com.senai.projetointegrador.features.users.User;
import br.com.senai.projetointegrador.features.users.UserRepository;
import br.com.senai.projetointegrador.security.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureRestTestClient
public abstract class IntegrationTestBase {
    protected static User TEACHER;
    protected static User COORDINATOR;
    protected static User QUALITY_ANALYST;
    protected static User ADMIN;

    @Autowired
    protected RestTestClient client;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected JwtTokenService jwtService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("TRUNCATE TABLE users");
        jdbcTemplate.execute("TRUNCATE TABLE students");
        jdbcTemplate.execute("TRUNCATE TABLE courses");
        jdbcTemplate.execute("TRUNCATE TABLE course_students");
        jdbcTemplate.execute("TRUNCATE TABLE incidents");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

        TEACHER = new User("John", "teacher", "teacher", Role.TEACHER);
        COORDINATOR = new User("John", "coord", "coord", Role.COORDINATOR);
        ADMIN = new User("John", "admin", "admin", Role.ADMINISTRATIVE_TEACHER);
        QUALITY_ANALYST = new User("John", "qa", "qa", Role.QUALITY_ANALYST);
    }

    protected User addUserToDb(String name, String login, String rawPassword, Role role) {
        var password = passwordEncoder.encode(rawPassword);
        var user = new User(name, login, password, role);
        return userRepository.save(user);
    }

    protected String getTokenFor(User user) {
        return jwtService.generateTokenFor(user);
    }

    protected void assertSingleValidationErrorFor(String fieldName, RestTestClient.RequestHeadersSpec<?> spec) {
        spec.exchange().expectStatus().isBadRequest().expectBody(ProblemDetail.class).value(body -> {
            assertThat(body).isNotNull();
            assertThat(body.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

            var properties = body.getProperties();
            assertThat(properties).isNotNull();
            assertThat(properties.size()).isOne();

            assertThat(properties.get("errors")).isNotNull();
            var errors = getValidationErrors(body);
            assertThat(errors.size()).isOne();
            assertThat(errors).extracting(FieldErrorDetail::field).contains(fieldName);
        });
    }

    private List<FieldErrorDetail> getValidationErrors(ProblemDetail problemDetail) {
        Object errors = problemDetail.getProperties().get("errors");
        if (errors == null) return List.of();

        return objectMapper.convertValue(
                errors,
                new TypeReference<List<FieldErrorDetail>>() {
                }
        );
    }
}
