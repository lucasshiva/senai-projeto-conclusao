package br.com.senai.projetointegrador.features.course.enrollStudent;

public class EnrollException extends RuntimeException {
    public EnrollException(Long studentId, Long courseId) {
        super("Student with ID " + studentId + " is already enrolled in course with ID " + courseId);
    }
}
