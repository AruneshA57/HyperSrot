package com.example.srot.data.repository;

import com.example.srot.data.model.Investor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface InvestorRepository extends CrudRepository<Investor, Long> {

    Optional<Investor> findByEmail(String email);
    Optional<Investor> findByPhoneNumber(String phoneNumber);
    Optional<Investor> findByReferralCode(String referralCode);
    List<Investor> findByJoiningCode(String joiningCode);
    @Query("Select count(i.id) FROM Investor i WHERE i.joiningDate = :today")
    Integer todayCount(@Param("today") Date today);

}
