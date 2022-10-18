package com.example.srot.data.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "investments")
public class Investment extends BaseEntity {

    @Column(name = "investment_tenure")
    private Integer investmentTenure;

    @Column(name = "investment_date")
    private Date investmentDate;

    @Column(name = "amount")
    private Long amount;

    @ManyToOne
    private Listing listing;

    @ManyToOne
    private Investor investor;

    @Column(name = "rate_per_unit")
    private Integer ratePerUnit;

    @Column(name = "power_generated")
    private Long powerGenerated;

    @OneToOne
    private Coupon coupon;

    private Long yield;

}
