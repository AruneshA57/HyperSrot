package com.example.srot.business.converter;

import com.example.srot.business.domain.InvestmentDisplay;
import com.example.srot.business.domain.InvestorPortfolio;
import com.example.srot.data.model.Investor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class InvestorToInvestorPortfolioConverter implements Converter<Investor, InvestorPortfolio> {

    private final InvestmentToInvestmentDisplayConverter investmentToInvestmentDisplayConverter;

    public InvestorToInvestorPortfolioConverter(InvestmentToInvestmentDisplayConverter investmentToInvestmentDisplayConverter) {
        this.investmentToInvestmentDisplayConverter = investmentToInvestmentDisplayConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public InvestorPortfolio convert(Investor source) {
        final InvestorPortfolio investorPortfolio = new InvestorPortfolio();
        investorPortfolio.setInvestorId(source.getId());
        source.getInvestments().forEach(investment -> {
            InvestmentDisplay investmentDisplay = investmentToInvestmentDisplayConverter.convert(investment);
            investorPortfolio.addInvestment(investmentDisplay);
        });
        return investorPortfolio;
    }
}
