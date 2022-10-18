package com.example.srot.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class InvestorCouponKey implements Serializable {

    @Column(name = "INVESTOR_ID")
    private long investorid;
    @Column(name = "COUPON_ID")
    private long couponid;

    public InvestorCouponKey() {
    }

    public InvestorCouponKey(Investor investor, Coupon coupon) {
        this.investorid = investor.getId();
        this.couponid = coupon.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvestorCouponKey that = (InvestorCouponKey) o;
        return investorid == that.investorid && couponid == that.couponid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(investorid, couponid);
    }
}
