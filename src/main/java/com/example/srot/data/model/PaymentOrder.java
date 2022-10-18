package com.example.srot.data.model;

import com.razorpay.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class PaymentOrder extends BaseEntity{

    private String razorpayOrderId;

    private Integer amount;

    private String receiptId;

    private Integer amountPaid;

    private String currency;

    private String status;

    private Integer attempts;

    private Date orderTimestamp;

    private Date paymentTimestamp;

    @ManyToOne
    private Wallet wallet;

    private String razorpayPaymentId;

    public PaymentOrder(Order order) {
        razorpayOrderId = order.get("id");
        amount = order.get("amount");
        receiptId = order.get("receipt");
        status = order.get("status");
        attempts = order.get("attempts");
        //System.out.println(order.get("created_at").toString());
        //System.out.println(order.get("created_at").getClass().getSimpleName());
        orderTimestamp = new Date(((java.util.Date)order.get("created_at")).getTime());
    }
}
