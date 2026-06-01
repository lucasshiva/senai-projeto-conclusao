package br.com.senai.projetointegrador.features.course;

import br.com.senai.projetointegrador.features.course.enrollStudent.EnrollStudentHandler;
import br.com.senai.projetointegrador.features.course.enrollStudent.EnrollStudentRequest;
import br.com.senai.projetointegrador.features.course.enrollStudent.EnrollStudentResponse;
import br.com.senai.projetointegrador.features.course.register.RegisterCourseHandler;
import br.com.senai.projetointegrador.features.course.register.RegisterCourseRequest;
import br.com.senai.projetointegrador.features.course.register.RegisterCourseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final RegisterCourseHandler registerCourseHandler;
    private final EnrollStudentHandler enrollStudentHandler;

    @PostMapping
    public ResponseEntity<RegisterCourseResponse> register(
            @RequestBody RegisterCourseRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var response = registerCourseHandler.handle(request);
        var uri = uriBuilder.path("/courses/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/{id}/students")
    public ResponseEntity<EnrollStudentResponse> addStudentToCourse(
            @PathVariable Long id,
            @RequestBody @Valid EnrollStudentRequest request
    ) {
        var response = enrollStudentHandler.handle(id, request);
        return ResponseEntity.ok(response);
    }
}
