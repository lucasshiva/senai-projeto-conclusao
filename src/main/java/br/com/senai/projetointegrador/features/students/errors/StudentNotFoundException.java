package br.com.senai.projetointegrador.features.students.errors;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Student with id " + id + " does not exist");
    }
}
