package br.com.senai.projetointegrador.features.students.register;

import br.com.senai.projetointegrador.config.storage.StorageProperties;
import br.com.senai.projetointegrador.features.students.Student;
import br.com.senai.projetointegrador.features.students.StudentRepository;
import br.com.senai.projetointegrador.features.students.register.exceptions.InvalidPictureException;
import br.com.senai.projetointegrador.features.students.register.exceptions.PictureStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
        MultipartFile picture = cmd.picture();

        if (!validContentTypes.contains(picture.getContentType())) {
            throw new InvalidPictureException("Picture is not a valid content type: " + picture.getContentType());
        }

        if (picture.getSize() <= 0) {
            throw new InvalidPictureException("Invalid picture size: " + picture.getSize());
        }

        InputStream stream;
        try {
            stream = picture.getInputStream();
        } catch (IOException e) {
            throw new InvalidPictureException("Invalid picture stream: " + e.getMessage());
        }

        Path root = storageProperties.studentPictures();
        try {
            Files.createDirectories(root);
        } catch (IOException ex) {
            throw new PictureStorageException(ex.getMessage());
        }

        String extension = getExtension(picture.getOriginalFilename());
        String filename = UUID.randomUUID() + extension;

        Path target = root.resolve(filename);
        try {
            Files.copy(stream, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new PictureStorageException(ex.getMessage());
        }

        var student = new Student(request.name(), target.toString(), request.birthDate());
        var saved = studentRepository.save(student);
        return new RegisterStudentResponse(
                saved.getId(), saved.getName(), saved.getPicture(), saved.getBirthDate());
    }

    private String getExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf("."));
    }
}
