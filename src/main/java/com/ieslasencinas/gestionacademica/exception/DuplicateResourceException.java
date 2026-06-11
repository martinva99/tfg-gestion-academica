package com.ieslasencinas.gestionacademica.exception;


public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String mensaje) {
        super(mensaje);
    }
}
