package br.com.senai.projetointegrador;

import br.com.senai.projetointegrador.features.auth.AuthController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
class ProjetoIntegradorApplicationTests {
    private final AuthController authController;

    @Test
    void contextLoads() {
        assertThat(authController).isNotNull();
    }
}
