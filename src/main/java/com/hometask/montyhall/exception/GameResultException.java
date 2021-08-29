package com.hometask.montyhall.exception;

public class GameResultException extends RuntimeException {

    public GameResultException() {
    }

    public GameResultException(String message) {
        super(message);
    }

    public GameResultException(String message, Throwable cause) {
        super(message, cause);
    }
}
