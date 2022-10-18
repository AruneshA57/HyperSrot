package com.example.srot.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "wallet")
public class Wallet extends BaseEntity {

    @OneToOne
    private Investor investor;

    @Column(name = "wallet_balance")
    private Long walletBalance;

    @Column(name = "joining_bonus")
    private Long joiningBonus;

    @Column(name = "referrals_bonus")
    private Long referralBonus;

    @Column(name = "questionnaire_bonus")
    private Long questionnaireBonus;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "wallet", fetch = FetchType.EAGER)
    private Set<RazorpayCreditTransaction> rzpayCreditTransactions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "wallet", fetch = FetchType.EAGER)
    private Set<DebitInvestmentTransaction> debitInvestmentTransactions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "wallet", fetch = FetchType.EAGER)
    private Set<RazorpayDebitTransaction> rzpayDebitTransactions = new HashSet<>();

    public Wallet() {
        walletBalance = 0L;
        this.joiningBonus = 0L;
        this.referralBonus = 0L;
        this.questionnaireBonus = 0L;
    }

    public Wallet(Investor investor) {
        this.investor = investor;
        this.walletBalance = 0L;
        this.joiningBonus = 0L;
        this.referralBonus = 0L;
        this.questionnaireBonus = 0L;
    }

    public Long getTotalBalance() {
        return walletBalance + joiningBonus + referralBonus;
    }

    public void addDebitInvestmentTransaction(DebitInvestmentTransaction debitInvestmentTransaction) {
        debitInvestmentTransactions.add(debitInvestmentTransaction);
    }

    public void addRazorpayCreditTransaction(RazorpayCreditTransaction razorpayCreditTransaction) {
        rzpayCreditTransactions.add(razorpayCreditTransaction);
    }

    public void addRazorpayDebitTransaction(RazorpayDebitTransaction razorpayDebitTransaction) {
        rzpayDebitTransactions.add(razorpayDebitTransaction);
    }

    public void addReferralBonus(Long amount) {
        referralBonus += amount;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "investor=" + investor +
                ", walletBalance=" + walletBalance +
                ", joiningBonus=" + joiningBonus +
                ", referralBonus=" + referralBonus +
                ", questionnaireBonus=" + questionnaireBonus +
                ", rzpayCreditTransactions=" + rzpayCreditTransactions +
                ", debitInvestmentTransactions=" + debitInvestmentTransactions +
                ", rzpayDebitTransactions=" + rzpayDebitTransactions +
                '}';
    }
}
