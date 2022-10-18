package com.example.srot.data.exceptions;

public class WalletBalanceInsufficientException extends RuntimeException {

    public WalletBalanceInsufficientException(String amountAvailable) {
        super("Insufficient wallet balance: " + amountAvailable);
    }
}
