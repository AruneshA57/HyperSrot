package com.example.srot.data.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "razorpay_fund_account")
public class RazorpayFundAccount extends BaseEntity {

    private String fundId;
    private boolean active;
    private String timestamp;
}
