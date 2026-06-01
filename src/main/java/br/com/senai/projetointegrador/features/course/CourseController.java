package br.com.senai.projetointegrador.features.course;

import br.com.senai.projetointegrador.features.course.register.RegisterCourseHandler;
import br.com.senai.projetointegrador.features.course.register.RegisterCourseRequest;
import br.com.senai.projetointegrador.features.course.register.RegisterCourseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final RegisterCourseHandler registerCourseHandler;

    @PostMapping
    public ResponseEntity<RegisterCourseResponse> register(
            @RequestBody RegisterCourseRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var response = registerCourseHandler.handle(request);
        var uri = uriBuilder.path("/courses/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }
}
