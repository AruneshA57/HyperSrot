package com.example.srot.business.domain;

import com.example.srot.data.model.Investor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvestorProfile {

    private Long investorId;

    private String name;

    private String email;

    private String phoneNumber;

    private LocalDate joiningDate;

    private String referralCode;

    private String bankKYCStatus;

    private String aadharKYCStatus;

    private String panKYCStatus;

    public InvestorProfile(Investor entity) {
        this.investorId = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.joiningDate = entity.getJoiningDate().toLocalDate();
        this.referralCode = entity.getReferralCode();
        this.aadharKYCStatus= String.valueOf(entity.getAadharKYCStatus());
        this.panKYCStatus = String.valueOf(entity.getPanKYCStatus());
        if(entity.getBankKYC() == null) this.setBankKYCStatus("PENDING");
        else this.bankKYCStatus=entity.getBankKYC().getStatus().toString();
    }
}
