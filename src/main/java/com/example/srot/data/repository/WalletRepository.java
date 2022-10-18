package com.example.srot.data.repository;

import com.example.srot.data.model.Investor;
import com.example.srot.data.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet findByInvestor(Investor investor);

}
