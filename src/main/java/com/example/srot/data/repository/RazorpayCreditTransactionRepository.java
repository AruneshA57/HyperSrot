package com.example.srot.data.repository;

import com.example.srot.data.model.RazorpayCreditTransaction;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RazorpayCreditTransactionRepository extends CrudRepository<RazorpayCreditTransaction, Long> {

    Optional<RazorpayCreditTransaction> findByOrderId(String orderId);
    Iterable<RazorpayCreditTransaction> findAllByStatus(String status);
}
