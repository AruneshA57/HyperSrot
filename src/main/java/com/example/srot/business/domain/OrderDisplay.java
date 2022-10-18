package com.example.srot.business.domain;

import com.example.srot.data.model.PaymentOrder;
import com.example.srot.data.model.RazorpayCreditTransaction;
import com.example.srot.data.utils.CurrencyConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDisplay {
    private Long orderId;
    private String razorpayOrderId;
    private String status;
    private String amount;
    private String timestamp; //this is payment timestamp
    private String razorpayPaymentId;

    public OrderDisplay(RazorpayCreditTransaction razorpayCreditTransaction) {
        orderId = razorpayCreditTransaction.getId();
        razorpayOrderId = razorpayCreditTransaction.getOrderId();
        status = razorpayCreditTransaction.getStatus();
        amount = CurrencyConverter.paiseLongToRupeeString(razorpayCreditTransaction.getAmount());
        timestamp = razorpayCreditTransaction.getTimestamp().toString();
        razorpayPaymentId = razorpayCreditTransaction.getPaymentId();
    }
}
