package com.example.srot.business.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankKycDto {

    // I don't think the below id variable is neccessary - to be removed if it work properly
    //private String id;
    private String accountName;
    private String accountNumber;
    private String ifsc;
}
