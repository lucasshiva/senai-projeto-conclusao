package br.com.senai.projetointegrador.features.students;

import br.com.senai.projetointegrador.features.students.register.RegisterStudentCommand;
import br.com.senai.projetointegrador.features.students.register.RegisterStudentHandler;
import br.com.senai.projetointegrador.features.students.register.RegisterStudentRequest;
import br.com.senai.projetointegrador.features.students.register.RegisterStudentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final RegisterStudentHandler registerStudentHandler;

    public ResponseEntity<RegisterStudentResponse> register(
            @RequestPart @Valid RegisterStudentRequest request, @RequestPart MultipartFile picture) {
        var command = new RegisterStudentCommand(request, picture);
        var response = registerStudentHandler.handle(command);
        return ResponseEntity.ok(response);
    }
}
