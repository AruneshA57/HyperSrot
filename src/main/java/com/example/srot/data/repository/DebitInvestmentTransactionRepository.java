package com.example.srot.data.repository;

import com.example.srot.data.model.DebitInvestmentTransaction;
import org.springframework.data.repository.CrudRepository;

public interface DebitInvestmentTransactionRepository
        extends CrudRepository<DebitInvestmentTransaction, Long> {
}
