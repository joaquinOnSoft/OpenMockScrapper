package com.openmock;

public class OpenMockException extends Exception {
    public OpenMockException(String message) {
        super(message);
    }

    public OpenMockException(Throwable cause) {
        super(cause);
    }
}