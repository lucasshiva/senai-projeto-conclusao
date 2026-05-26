package br.com.senai.projetointegrador.features.students.register;

import org.springframework.web.multipart.MultipartFile;

public record RegisterStudentCommand(
        RegisterStudentRequest request,
        MultipartFile picture
) {}
