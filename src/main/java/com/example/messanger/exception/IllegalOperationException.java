package com.example.messanger.exception;

public class IllegalOperationException extends RuntimeException {

    public IllegalOperationException() {
        super("You are not allowed to perform this operation");
    }
}
