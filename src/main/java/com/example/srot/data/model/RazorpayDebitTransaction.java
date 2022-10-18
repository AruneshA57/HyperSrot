package com.example.srot.data.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "razorpay_debit_transaction")
public class RazorpayDebitTransaction extends Transaction {
    private Date requestTimestamp;
    private String idempotencyKey;
    private String payoutId;
    private Long fees;
    private Long tax;
    private String status;
    private String narration;
    private String failureReason;
    private String utr;
}
