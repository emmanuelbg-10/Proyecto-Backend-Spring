package com.emmanuel.biblioteca.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);  // Llama al constructor de RuntimeException con el mensaje
    }
}
