package br.com.senai.projetointegrador.features.students.register;

import br.com.senai.projetointegrador.config.storage.StorageProperties;
import br.com.senai.projetointegrador.errors.FileStorageException;
import br.com.senai.projetointegrador.features.students.Student;
import br.com.senai.projetointegrador.features.students.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegisterStudentHandler {
    public static List<String> validContentTypes = List.of("image/jpeg", "image/png");

    private final StudentRepository studentRepository;
    private final StorageProperties storageProperties;

    @Transactional
    public RegisterStudentResponse handle(RegisterStudentCommand cmd) {
        var request = cmd.request();

        try {
            MultipartFile picture = cmd.picture();
            if (!validContentTypes.contains(picture.getContentType())) {
                throw new BadRequestException("Picture is not a valid content type: " + picture.getContentType());
            }

            Path root = storageProperties.studentPictures();
            Files.createDirectories(root);

            String extension = getExtension(picture.getOriginalFilename());
            String filename = UUID.randomUUID() + extension;

            Path target = root.resolve(filename);
            Files.copy(picture.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            var student = new Student(request.name(), target.toString(), request.birthDate());
            var saved = studentRepository.save(student);
            return new RegisterStudentResponse(
                    saved.getId(), saved.getName(), saved.getPicture(), saved.getBirthDate());

        } catch (IOException ex) {
            throw new FileStorageException("Couldn't register student: " + ex.getMessage());
        }
    }

    private String getExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf("."));
    }
}
