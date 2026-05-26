package br.com.senai.projetointegrador.features.students.register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record RegisterStudentRequest(
        @NotBlank String name, @NotNull LocalDate birthDate, MultipartFile picture) {}
