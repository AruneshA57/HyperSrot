package com.example.srot.data.repository;

import com.example.srot.data.model.Investment;
import org.springframework.data.repository.CrudRepository;

public interface InvestmentRepository extends CrudRepository<Investment, Long> {
}
