package com.evaluacion.users_api.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("El correo ya esta registrado");
    }
}
