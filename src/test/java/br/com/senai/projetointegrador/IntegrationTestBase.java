package br.com.senai.projetointegrador;

import br.com.senai.projetointegrador.errors.FieldErrorDetail;
import br.com.senai.projetointegrador.features.auth.login.LoginRequest;
import br.com.senai.projetointegrador.features.auth.login.LoginResponse;
import br.com.senai.projetointegrador.features.auth.register.RegisterRequest;
import br.com.senai.projetointegrador.features.auth.register.RegisterResponse;
import br.com.senai.projetointegrador.features.users.Role;
import br.com.senai.projetointegrador.features.users.User;
import br.com.senai.projetointegrador.features.users.UserRepository;
import br.com.senai.projetointegrador.security.JwtTokenService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.validation.FieldError;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureRestTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationTestBase {
    protected static User ADMIN_USER = new User();
    protected static User COORDINATOR_USER = new User();
    protected static User TEACHER_USER = new User();
    protected static User QA_USER = new User();

    protected String adminToken;
    protected String coordinatorToken;
    protected String qaToken;
    protected String teacherToken;

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

    private void cleanDatabase() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("TRUNCATE TABLE users");
        jdbcTemplate.execute("TRUNCATE TABLE students");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    private void truncateSafeTables() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("TRUNCATE TABLE students");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    @BeforeEach
    void setUp() {
        truncateSafeTables();
    }

    @BeforeAll
    void beforeAll() {
        cleanDatabase();

        ADMIN_USER = addUserToDb("admin", "admin", "admin", Role.ADMINISTRATIVE_TEACHER);
        COORDINATOR_USER = addUserToDb("coord", "coord", "coord", Role.COORDINATOR);
        TEACHER_USER = addUserToDb("teacher", "teacher", "teacher", Role.TEACHER);
        QA_USER = addUserToDb("qa", "qa", "qa", Role.QUALITY_ANALYST);

        adminToken = getTokenFor(ADMIN_USER);
        coordinatorToken = getTokenFor(COORDINATOR_USER);
        teacherToken = getTokenFor(TEACHER_USER);
        qaToken = getTokenFor(QA_USER);
    }

    protected User addUserToDb(String name, String login, String rawPassword, Role role) {
        var password = passwordEncoder.encode(rawPassword);
        var user = new User(name, login, password, role);
        return userRepository.save(user);
    }

    protected User addUserToDb(String login, String rawPassword, Role role) {
        var password = passwordEncoder.encode(rawPassword);
        var user = new User("test", login, password, role);
        return userRepository.save(user);
    }

    protected User addUserToDb(String login, String rawPassword) {
        var password = passwordEncoder.encode(rawPassword);
        var user = new User("test", login, password, Role.TEACHER);
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
