package com.example.srot.business.domain;

import com.example.srot.data.model.Investor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestorReferralDetails {

    private String referralCode;
    private Integer friendsJoined;
    private Integer friendsInvested;
    private Double joiningBonus;
    private Double availableJoiningBonus;
    private Double referralBonus;
    private Double availableReferralBonus;



    public InvestorReferralDetails(String referralCode,Long availableJoiningBonus) {
        this.friendsJoined = 0;
        this.friendsInvested = 0;
        this.referralBonus = 0.0;
        this.availableJoiningBonus = availableJoiningBonus==null?0.0:(double)(availableJoiningBonus/100);
        this.availableReferralBonus = 0.0;
        this.joiningBonus = 0.0;
        this.referralCode = referralCode;

    }

}
