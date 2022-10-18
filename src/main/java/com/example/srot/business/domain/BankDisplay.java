package com.example.srot.business.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankDisplay {
    private String id;
    private String accountNumber;
    private String bankName;
    private String ifsc;
    private String kycStatus;
}
