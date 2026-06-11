package com.ieslasencinas.gestionacademica.exception;


public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String mensaje) {
        super(mensaje);
    }
}
