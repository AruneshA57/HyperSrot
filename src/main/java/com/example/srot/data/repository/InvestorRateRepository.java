package com.example.srot.data.repository;

import com.example.srot.data.model.InvestorRate;
import org.springframework.data.repository.CrudRepository;

public interface InvestorRateRepository extends CrudRepository<InvestorRate, Long> {
    public InvestorRate findByActiveAndTenure(boolean active, Integer tenure);
}
