package com.example.srot.data.repository;

import com.example.srot.data.model.InvestorReturns;
import org.springframework.data.repository.CrudRepository;

public interface InvestorReturnsRepository extends CrudRepository<InvestorReturns, Long> {
    public InvestorReturns findByActive(boolean active);
}
