package br.com.senai.projetointegrador;

import br.com.senai.projetointegrador.features.auth.login.LoginRequest;
import br.com.senai.projetointegrador.features.auth.login.LoginResponse;
import br.com.senai.projetointegrador.features.auth.register.RegisterRequest;
import br.com.senai.projetointegrador.features.auth.register.RegisterResponse;
import br.com.senai.projetointegrador.features.users.Role;
import br.com.senai.projetointegrador.features.users.User;
import br.com.senai.projetointegrador.features.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureRestTestClient
public abstract class IntegrationTestBase {
    @Autowired
    protected RestTestClient client;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("TRUNCATE TABLE users");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    private String adminToken;

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

    protected String login(String login, String rawPassword) {
        var request = new LoginRequest(login, rawPassword);
        var response = client.post()
                .uri("/auth/login")
                .body(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(LoginResponse.class)
                .returnResult()
                .getResponseBody();

        assert response != null;
        return response.token();
    }

    protected String register(String name, String login, String password, Role role) {
        var request = new RegisterRequest(name, login, password, role);
        var response = client.post()
                .uri("/auth/register")
                .body(request)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(RegisterResponse.class)
                .returnResult()
                .getResponseBody();

        assert response != null;
        return response.token();
    }

    protected String adminToken() {
        if (adminToken != null) {
            return adminToken;
        }

        return register("admin", "admin", "admin", Role.ADMINISTRATIVE_TEACHER);
    }
}
