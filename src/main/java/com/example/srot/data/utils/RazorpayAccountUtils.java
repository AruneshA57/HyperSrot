package com.example.srot.data.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

public class RazorpayAccountUtils {
    @Value("${srot.rzpay.apiKey}")
    private String apiKey;

    @Value("${srot.rzpay.secretKey}")
    private String secretKey;

    @Value("${srot.rzpay.verificationAccount}")
    private String verificationAccountNumber;

    @Value("${srot.rzpay.payoutAccount}")
    private String payoutAccountNumber;

    public String getApiKey() {
        return apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getVerificationAccountNumber() {
        return verificationAccountNumber;
    }

    public String getPayoutAccountNumber() {
        return  payoutAccountNumber;
    }
}
