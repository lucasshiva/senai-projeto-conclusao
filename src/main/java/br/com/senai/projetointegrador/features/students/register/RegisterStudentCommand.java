package br.com.senai.projetointegrador.features.students.register;

import lombok.With;
import org.springframework.web.multipart.MultipartFile;

@With
public record RegisterStudentCommand(
        RegisterStudentRequest request,
        MultipartFile picture
) {
}
