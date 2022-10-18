package com.example.srot.data.repository;

import com.example.srot.data.model.Coupon;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CouponRepository extends CrudRepository<Coupon, Long> {

    public Optional<Coupon> findCouponByCouponCode(String couponCode);
}
