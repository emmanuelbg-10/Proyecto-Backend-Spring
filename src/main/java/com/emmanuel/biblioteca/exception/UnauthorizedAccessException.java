package com.emmanuel.biblioteca.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);  // Llama al constructor de RuntimeException con el mensaje
    }
}
