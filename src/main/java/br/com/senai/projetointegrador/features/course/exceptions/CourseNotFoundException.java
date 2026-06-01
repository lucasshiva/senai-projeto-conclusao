package br.com.senai.projetointegrador.features.course.exceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long courseId) {
        super("Course with id " + courseId + " does not exist");
    }
}
