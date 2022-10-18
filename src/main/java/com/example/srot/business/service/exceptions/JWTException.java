package com.example.srot.business.service.exceptions;

public class JWTException extends RuntimeException{

    public JWTException(String message) {
        super(message);
    }
}
