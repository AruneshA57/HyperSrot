package com.example.srot.business.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewUser {

    private String name;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String nationality;
    private String nominee;
    private String password;
    private String referralCode;
    private String otp;
    private String bankAccountNumber;
    private String bankIfsc;
    private Boolean tfa;

}
