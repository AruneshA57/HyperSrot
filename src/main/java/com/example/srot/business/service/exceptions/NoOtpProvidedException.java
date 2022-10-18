package com.example.srot.business.service.exceptions;

import com.example.srot.business.domain.NewUser;
import lombok.Getter;

@Getter
public class NoOtpProvidedException extends RuntimeException{

    public NoOtpProvidedException() {
        super("Sending OTP to user");
    }
}
