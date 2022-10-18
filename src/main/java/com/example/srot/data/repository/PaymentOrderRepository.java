package com.example.srot.data.repository;

import com.example.srot.data.model.PaymentOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PaymentOrderRepository extends CrudRepository<PaymentOrder, Long> {

    Optional<PaymentOrder> findByRazorpayOrderId(String orderId);
}
