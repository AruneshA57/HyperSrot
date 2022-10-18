package com.example.srot.business.service;

import com.example.srot.data.model.Coupon;
import com.example.srot.data.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Coupon validateCoupon(String couponCode) {
        Optional<Coupon> couponOptional = couponRepository.findCouponByCouponCode(couponCode);

        if(couponOptional.isEmpty()) {
            log.error("Invalid coupon code: " + couponCode);
            return null;
        }

        Coupon coupon = couponOptional.get();

        //check date
        LocalDate startDate = coupon.getStartDate().toLocalDate();
        LocalDate endDate = startDate.plusDays(coupon.getDuration());

        if(endDate.isAfter(LocalDate.now())) {
            log.error("Coupon " + couponCode + " is no longer valid");
            return null;
        }

        return coupon;
    }

}
