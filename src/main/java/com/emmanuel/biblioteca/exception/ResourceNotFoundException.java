package com.emmanuel.biblioteca.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);  // Llama al constructor de RuntimeException con el mensaje
    }
}
