package com.example.srot.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class InvestorCoupon {

    @EmbeddedId
    private InvestorCouponKey id;

    @MapsId("investorId")
    @ManyToOne
    @JoinColumn(name = "investor_id")
    private Investor investor;
    @MapsId("couponId")
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
    private boolean used;

    public InvestorCoupon() {}

    public InvestorCoupon(Investor investor, Coupon coupon, boolean used) {
        this.id = new InvestorCouponKey(investor,coupon);
        this.investor = investor;
        this.coupon = coupon;
        this.used = used;
    }
}
