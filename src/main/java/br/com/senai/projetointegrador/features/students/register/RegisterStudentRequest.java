package br.com.senai.projetointegrador.features.students.register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.With;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@With
public record RegisterStudentRequest(
        @NotBlank String name, @NotNull LocalDate birthDate) {
}
