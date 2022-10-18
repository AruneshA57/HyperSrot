package com.example.srot.business.converter;

import com.example.srot.business.domain.InvestorProfile;
import com.example.srot.data.model.Investor;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class InvestorToInvestorProfileConverter implements Converter<Investor, InvestorProfile> {

    @Synchronized
    @Nullable
    @Override
    public InvestorProfile convert(Investor source) {
        if(source == null) {
            return null;
        }

        final InvestorProfile investorProfile = new InvestorProfile();
        investorProfile.setInvestorId(source.getId());
        investorProfile.setEmail(source.getEmail());
        investorProfile.setName(source.getName());
        investorProfile.setJoiningDate(source.getJoiningDate().toLocalDate());
        investorProfile.setPhoneNumber(source.getPhoneNumber());
        investorProfile.setAadharKYCStatus(source.getAadharKYCStatus().name());
        investorProfile.setPanKYCStatus(source.getPanKYCStatus().name());

        return investorProfile;
    }
}
