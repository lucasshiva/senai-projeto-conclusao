package br.com.senai.projetointegrador.features.students.register;

import br.com.senai.projetointegrador.config.storage.StorageProperties;
import br.com.senai.projetointegrador.features.students.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterStudentHandlerTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StorageProperties storageProperties;

    @TempDir
    Path tempDir;

    private RegisterStudentHandler handler;

    @BeforeEach
    void setUp() {
        handler = new RegisterStudentHandler(studentRepository, storageProperties);
    }

    @Test()
    void shouldSaveToDatabaseWhenFileIsStoredSuccessfully() {
        when(storageProperties.studentPictures()).thenReturn(tempDir);

        var birthDate = LocalDate.of(2005, 1, 1);
        var request = new RegisterStudentRequest("John", birthDate);
        var picture = new MockMultipartFile("photo", "filename.png", "image/png", "data".getBytes());
        var command = new RegisterStudentCommand(request, picture);

        when(studentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var response = handler.handle(command);

        assertNotNull(response);
        verify(studentRepository).save(any());
        assertTrue(Files.exists(Path.of(response.picture())));
    }
}