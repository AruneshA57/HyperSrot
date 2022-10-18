package com.example.srot.data.repository;

import com.example.srot.data.model.Investor;
import com.example.srot.data.model.Referral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReferralRepository extends JpaRepository<Referral,Long> {

    List<Referral> findByReferrer(Investor referrer);
    Optional<Referral> findByReferee(Investor Referee);

}
