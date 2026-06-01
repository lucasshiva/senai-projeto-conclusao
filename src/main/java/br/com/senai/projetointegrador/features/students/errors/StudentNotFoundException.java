package br.com.senai.projetointegrador.features.students.errors;

import br.com.senai.projetointegrador.features.students.StudentException;

public class StudentNotFoundException extends StudentException {
    public StudentNotFoundException(Long id) {
        super("Student with id " + id + " does not exist");
    }
}
