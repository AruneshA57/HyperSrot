package com.example.srot.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Coupon extends BaseEntity {

    @Column(name = "coupon_code", unique = true)
    private String couponCode;

    @Column(name = "discount_amount")
    private Integer discountAmount;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "duration")
    private Integer duration;

}
