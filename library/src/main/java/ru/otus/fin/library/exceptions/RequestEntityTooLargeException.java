package ru.otus.fin.library.exceptions;

public class RequestEntityTooLargeException extends RuntimeException {
    public RequestEntityTooLargeException(String message) {
        super(message);
    }
}
