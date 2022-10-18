package com.example.srot.data.exceptions;

public class SignatureMismatchException extends Exception {

    public SignatureMismatchException(String orderId) {
        super("Signature mismatch for order: " + orderId);
    }
}
