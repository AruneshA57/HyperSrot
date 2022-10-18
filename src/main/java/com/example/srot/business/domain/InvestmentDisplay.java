package com.example.srot.business.domain;

import com.example.srot.data.model.Investment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvestmentDisplay {
    private Long investmentId;
    private Integer tenure;
    private String date;
    private Long amount;
    private Double yield;
    private Long solarAssets;
    private Long contractId;
    private String status;

    public InvestmentDisplay(Investment investment) {
        this.investmentId = investment.getId();
        this.tenure = investment.getInvestmentTenure();
        this.date = investment.getInvestmentDate().toString();
        this.amount = investment.getAmount();
        this.yield = (double)investment.getYield() / 100;
        this.solarAssets = investment.getAmount() / investment.getListing().getInvestorReturns().getCostPerKW();
        this.contractId = investment.getListing().getId();
        this.status = investment.getListing().getListingStatus().toString();
    }
}
