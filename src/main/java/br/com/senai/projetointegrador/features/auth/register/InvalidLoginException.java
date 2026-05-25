package br.com.senai.projetointegrador.features.auth.register;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException() {
        super("An user with that login already exists");
    }
}
