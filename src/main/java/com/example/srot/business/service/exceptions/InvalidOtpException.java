package com.example.srot.business.service.exceptions;

public class InvalidOtpException extends UserInfoException {

    public InvalidOtpException() {
        super("Provided Otp was not valid");
    }
}
