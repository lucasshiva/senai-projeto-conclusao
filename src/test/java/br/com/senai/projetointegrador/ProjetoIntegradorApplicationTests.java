package br.com.senai.projetointegrador;

import br.com.senai.projetointegrador.features.auth.AuthController;
import br.com.senai.projetointegrador.features.course.CourseController;
import br.com.senai.projetointegrador.features.incidents.IncidentController;
import br.com.senai.projetointegrador.features.students.StudentController;
import br.com.senai.projetointegrador.features.users.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProjetoIntegradorApplicationTests {
    @Autowired
    private AuthController authController;

    @Autowired
    private UserController userController;

    @Autowired
    private StudentController studentController;

    @Autowired
    private CourseController courseController;

    @Autowired
    private IncidentController incidentController;

    @Test
    void contextLoads() {
        assertThat(authController).isNotNull();
        assertThat(userController).isNotNull();
        assertThat(studentController).isNotNull();
        assertThat(courseController).isNotNull();
        assertThat(incidentController).isNotNull();
    }
}
