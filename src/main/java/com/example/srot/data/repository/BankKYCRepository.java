package com.example.srot.data.repository;

import com.example.srot.data.enums.KYCStatus;
import com.example.srot.data.model.BankKYC;
import org.springframework.data.repository.CrudRepository;

public interface BankKYCRepository extends CrudRepository<BankKYC, Long> {

    Iterable<BankKYC> findAllByStatus(Enum<KYCStatus> status);
}
