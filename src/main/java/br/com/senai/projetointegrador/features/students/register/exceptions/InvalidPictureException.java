package br.com.senai.projetointegrador.features.students.register.exceptions;

import br.com.senai.projetointegrador.features.students.StudentException;

public class InvalidPictureException extends StudentException {
    public InvalidPictureException(String message) {
        super(message);
    }
}
