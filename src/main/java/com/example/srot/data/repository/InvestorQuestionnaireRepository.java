package com.example.srot.data.repository;

import com.example.srot.data.model.Investor;
import com.example.srot.data.model.InvestorQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface InvestorQuestionnaireRepository extends JpaRepository<InvestorQuestionnaire, Long> {

    Optional<InvestorQuestionnaire> findByInvestor(Investor investor);
}
