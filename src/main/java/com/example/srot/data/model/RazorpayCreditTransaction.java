package com.example.srot.data.model;

import com.razorpay.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "razorpay_credit_transaction")
public class RazorpayCreditTransaction extends Transaction{
    private String orderId;
    private String paymentId;
    private Long amountPaid;
    private String currency;
    private Integer numOfAttempts;
    private Date orderTimestamp;

    public void setRazorpayOrderDetails(Order order) {
        orderId = order.get("id");
        numOfAttempts = order.get("attempts");
        orderTimestamp = new Date(((java.util.Date)order.get("created_at")).getTime());
    }

    @Override
    public String toString() {
        return " RazorpayCreditTransaction{" +
                "orderId='" + orderId + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", amountPaid=" + amountPaid +
                ", currency='" + currency + '\'' +
                ", numOfAttempts=" + numOfAttempts +
                ", orderTimestamp=" + orderTimestamp +
                '}';
    }
}
