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
@Table(name = "razorpay_contact")
public class RazorpayContact extends BaseEntity{

    private String contactId;
    private boolean active;
    private String timestamp;
}
