package com.example.srot.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Getter
@Setter
@Entity
public class Referral extends BaseEntity{

    @OneToOne(fetch = FetchType.EAGER)
    private Investor referrer;
    @OneToOne(fetch = FetchType.EAGER)
    private Investor referee;
    private Long referralBonus;

    public Referral() {
    }

    public Referral(Investor referrer, Investor referee, Long referralBonus) {
        this.referrer = referrer;
        this.referee = referee;
        this.referralBonus = referralBonus;
    }
}
