package com.example.srot.business.converter;

import com.example.srot.business.domain.InvestmentDisplay;
import com.example.srot.data.model.Investment;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class InvestmentToInvestmentDisplayConverter implements Converter<Investment, InvestmentDisplay> {

    @Synchronized
    @Nullable
    @Override
    public InvestmentDisplay convert(Investment source) {
        InvestmentDisplay investmentDisplay = new InvestmentDisplay();
        /*investmentDisplay.setInvestmentId(source.getId());
        investmentDisplay.setInvestmentDate(source.getInvestmentDate().toString());
        investmentDisplay.setInvestmentTenure(source.getInvestmentTenure());
        investmentDisplay.setAmount(source.getAmount());
        investmentDisplay.setPowerGenerated(source.getPowerGenerated());
        investmentDisplay.setRatePerUnit(source.getRatePerUnit());
        investmentDisplay.setCouponCode(source.getCoupon().getCouponCode());*/
        return investmentDisplay;
    }
}
