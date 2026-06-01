package br.com.senai.projetointegrador.features.students;

import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    public StudentDto toDto(Student student) {
        return new StudentDto(student.getId(), student.getName(), student.getPicture(), student.getBirthDate());
    }
}
