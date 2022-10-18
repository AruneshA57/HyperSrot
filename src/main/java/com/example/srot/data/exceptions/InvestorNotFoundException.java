package com.example.srot.data.exceptions;

public class InvestorNotFoundException extends RuntimeException {

    public InvestorNotFoundException(Long investorId) {
        super("Investor with ID: " + investorId + " does not exist");
    }
}
