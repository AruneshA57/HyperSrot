package com.example.srot.data.model;

import com.example.srot.business.domain.NewUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "investors")
public class Investor extends Profile{

    @Column(name = "referral_code",unique = true)
    private String referralCode;

    @Column(name = "joining_code")
    private String joiningCode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "investor", fetch = FetchType.EAGER)
    private Set<Investment> investments = new HashSet<>();


    @OneToMany(mappedBy = "investor")
    private Set<InvestorCoupon> coupons;

    @Column(name="NOMINEE")
    private String nominee;

    @OneToOne
    private InvestorQuestionnaire questionaire;

    @OneToMany(mappedBy = "investor")
    private Set<Invoice> invoices;

    public void addInvestment(Investment investment) {
        investments.add(investment);
    }

    public Investor() {}

    public Investor(NewUser newUser) {
        setName(newUser.getName());
        setPhoneNumber(newUser.getPhone());
        setEmail(newUser.getEmail());
        setJoiningDate(Date.valueOf(LocalDate.now()));
        LocalDate temp = LocalDate.parse(newUser.getDateOfBirth(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        setDateOfBirth(temp==null?null:Date.valueOf(temp));
        setJoiningCode(newUser.getReferralCode());
        setTwoFactorAuthentication(false);
    }

    public void updateProfileInfo(NewUser newUserInfo){
        setName(newUserInfo.getName()==null?getName():newUserInfo.getName());
        LocalDate temp = LocalDate.parse(newUserInfo.getDateOfBirth(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        setDateOfBirth(temp==null?getDateOfBirth():Date.valueOf(temp));
        setPhoneNumber(newUserInfo.getPhone()==null?getPhoneNumber(): newUserInfo.getPhone());
        setEmail(newUserInfo.getEmail()==null?getEmail():newUserInfo.getEmail());
        setNominee(newUserInfo.getNominee()==null?getNominee():newUserInfo.getNominee());
        setNationality(newUserInfo.getNationality()==null?getNationality():newUserInfo.getNationality());
        setTwoFactorAuthentication(newUserInfo.getTfa()==null?getTwoFactorAuthentication():newUserInfo.getTfa());
        setBankIfsc(newUserInfo.getBankIfsc()==null?getBankIfsc():newUserInfo.getBankIfsc());
        setBankAccountNumber(newUserInfo.getBankAccountNumber()==null?getBankAccountNumber():newUserInfo.getBankAccountNumber());
    }

}
