package com.example.srot.data.model;

import com.example.srot.data.enums.KYCStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bank_kyc")
public class BankKYC extends BaseEntity {
    private String validationId;
    @Enumerated(EnumType.STRING)
    private KYCStatus status = KYCStatus.PENDING;
    private Long amount;
    private String currency;
    private String timestamp;

    @OneToOne
    private Investor investor;

    @OneToOne
    private Consumer consumer;

    @OneToOne(cascade = {CascadeType.ALL})
    private RazorpayDebitTransaction razorpayDebitTransaction;
}
