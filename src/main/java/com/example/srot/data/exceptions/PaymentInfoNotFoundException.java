package com.example.srot.data.exceptions;

import java.util.function.Supplier;

public class PaymentInfoNotFoundException extends Exception {

    public PaymentInfoNotFoundException(String orderId) {
        super("Payment info not found for order: " + orderId);
    }

}
