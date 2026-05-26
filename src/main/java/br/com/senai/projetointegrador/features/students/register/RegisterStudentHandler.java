package br.com.senai.projetointegrador.features.students.register;

import br.com.senai.projetointegrador.config.storage.StorageProperties;
import br.com.senai.projetointegrador.features.students.Student;
import br.com.senai.projetointegrador.features.students.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegisterStudentHandler {
    private final StudentRepository studentRepository;
    private final StorageProperties storageProperties;

    @Transactional
    public RegisterStudentResponse handle(RegisterStudentCommand cmd) {
        var request = cmd.request();

        try {

            Path root = storageProperties.studentPictures();
            Files.createDirectories(root);

            MultipartFile picture = cmd.picture();
            String extension = getExtension(picture.getOriginalFilename());
            String filename = UUID.randomUUID() + extension;

            Path target = root.resolve(filename);
            Files.copy(picture.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            var student = new Student(request.name(), target.toString(), request.birthDate());
            var saved = studentRepository.save(student);
            return new RegisterStudentResponse(
                    saved.getId(), saved.getName(), saved.getPicture(), saved.getBirthDate());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf("."));
    }
}
