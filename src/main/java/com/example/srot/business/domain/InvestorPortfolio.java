package com.example.srot.business.domain;

import com.example.srot.data.model.Investment;
import com.example.srot.data.model.Investor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class InvestorPortfolio {

    private Long investorId;
    private Long investedAmount;
    private Integer totalInvestments;
    private Long solarAssetsOwned;
    private List<InvestmentDisplay> investments = new ArrayList<>();

    public InvestorPortfolio() {
        this.investedAmount = 0L;
        this.totalInvestments = 0;
        this.solarAssetsOwned = 0L;
    }

    public InvestorPortfolio(Investor investor) {
        this.investorId = investor.getId();
        investor.getInvestments().forEach(investment -> {
            addInvestment(new InvestmentDisplay(investment));
        });
    }

    public void addInvestment(InvestmentDisplay investment) {
        this.investments.add(investment);
        this.investedAmount += investment.getAmount();
        ++this.totalInvestments;
        this.solarAssetsOwned += investment.getSolarAssets();
    }
}
