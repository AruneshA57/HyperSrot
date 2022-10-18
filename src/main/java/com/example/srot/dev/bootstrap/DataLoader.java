package com.example.srot.dev.bootstrap;

import com.example.srot.data.enums.KYCStatus;
import com.example.srot.data.enums.ListingStatus;
import com.example.srot.data.enums.Role;
import com.example.srot.data.model.*;
import com.example.srot.data.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
//@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final ListingRepository listingRepository;
    private final CouponRepository couponRepository;
    private final InvestorRepository investorRepository;
    private final InvestorRateRepository investorRateRepository;
    private final InvestorReturnsRepository investorReturnsRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(ListingRepository listingRepository,
                      CouponRepository couponRepository,
                      InvestorRepository investorRepository,
                      InvestorRateRepository investorRateRepository,
                      InvestorReturnsRepository investorReturnsRepository,
                      PasswordEncoder passwordEncoder) {
        this.listingRepository = listingRepository;
        this.couponRepository = couponRepository;
        this.investorRepository = investorRepository;
        this.investorRateRepository = investorRateRepository;
        this.investorReturnsRepository = investorReturnsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadReturns();
        investorRepository.saveAll(loadData());
        listingRepository.saveAll(getListings());
        log.debug("Loading bootstrap data");
    }

    public void loadReturns() {

    }

    public List<Investor> loadData() {
        List<Investor> investors = new ArrayList<>();

        Investor investorBhavna = new Investor();
        investorBhavna.setName("Bhavna Rana");
        investorBhavna.setEmail("xxxx@gmail.com");
        investorBhavna.setPhoneNumber("9999999999");
        investorBhavna.setJoiningDate(new Date(System.currentTimeMillis()));
        LocalDate temp = LocalDate.parse("1991-11-31", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        investorBhavna.setDateOfBirth(Date.valueOf(temp));
        investorBhavna.setRole(Role.INVESTOR);
        investorBhavna.setPassword(passwordEncoder.encode("qwerty123"));
        investorBhavna.setAccountNonExpired(true);
        investorBhavna.setAccountNonLocked(true);
        investorBhavna.setCredentialNonExpired(true);
        investorBhavna.setEnabled(true);
        investorBhavna.setEmailConfirmed(false);
        //investorBhavna.setBankKYCStatus(KYCStatus.COMPLETE);
        investorBhavna.setAadharKYCStatus(KYCStatus.COMPLETE);
        investorBhavna.setPanKYCStatus(KYCStatus.COMPLETE);
        investorBhavna.setBankAccountName("Bhavna Rana");
        investorBhavna.setBankAccountNumber("11111111");
        investorBhavna.setBankIfsc("IFSC111");
        investorBhavna.setBankName("BANK1");
        investorBhavna.setJoiningCode("");

        Wallet walletBhavna = new Wallet();
        investorBhavna.setWallet(walletBhavna);
        walletBhavna.setInvestor(investorBhavna);

        investors.add(investorBhavna);

        Investor investorTwinkle = new Investor();
        investorTwinkle.setName("Twinkle Agarwal");
        investorTwinkle.setEmail("xxxx@gmail.com");
        investorTwinkle.setPhoneNumber("9999999999");
        investorTwinkle.setJoiningDate(Date.valueOf("2021-11-08"));
        //investorTwinkle.setBankKYCStatus(KYCStatus.PENDING);
        investorTwinkle.setAadharKYCStatus(KYCStatus.COMPLETE);
        investorTwinkle.setPanKYCStatus(KYCStatus.COMPLETE);
        investorTwinkle.setBankAccountName("Twinkle Agarwal");
        investorTwinkle.setBankAccountNumber("22222222");
        investorTwinkle.setBankIfsc("IFSC222");
        investorTwinkle.setBankName("BANK2");
        investorTwinkle.setJoiningCode("");

        investors.add(investorTwinkle);

        return investors;
    }

    public List<Listing> getListings() {
        List<Listing> listings = new ArrayList<>();

        Listing l1 = new Listing();
        l1.setStartDate(Date.valueOf("2021-12-05"));
        l1.setEndDate(Date.valueOf("2021-12-30"));
        //l1.setDuration(15);
        l1.setFundRaised(0L);
        //l1.setFundLimit(5000000L);
        l1.setListingStatus(ListingStatus.FUNDING);

        listings.add(l1);

        InvestorRate investorRate60 = new InvestorRate();
        investorRate60.setActive(true);
        investorRate60.setTenure(60);
        investorRate60.setInvestorRate(560L);

        investorRateRepository.save(investorRate60);

        InvestorRate investorRate30 = new InvestorRate();
        investorRate30.setActive(true);
        investorRate30.setTenure(30);
        investorRate30.setInvestorRate(485L);

        investorRateRepository.save(investorRate30);

        InvestorReturns investorReturns = new InvestorReturns();
        investorReturns.setActive(true);
        investorReturns.setCostPerKW(5250000L);
        investorReturns.setPowerDepreciation(1L);
        investorReturns.setTds(2L);
        investorReturns.setUnitsPerDayPerKW(4L);
        investorReturns.setValueDepreciationPerYear(1L);
        investorReturns.setPowerDepreciationIntervalInMonths(12L);

        investorReturnsRepository.save(investorReturns);

        l1.setInvestorRate30(investorRate30);
        l1.setInvestorRate60(investorRate60);
        l1.setInvestorReturns(investorReturns);

        Plant p1 = new Plant();
        p1.setTags("RESIDENTIAL");
        p1.setPlantCapacity(100000L);
        p1.setPlantCost(1000000000L);
        p1.setListing(l1);

        Plant p2 = new Plant();
        p2.setTags("HOSPITAL");
        p2.setPlantCapacity(200000L);
        p2.setPlantCost(2000000000L);
        p2.setListing(l1);

        l1.addPlant(p1);
        l1.addPlant(p2);

        Investment i1 = new Investment();
        i1.setAmount(100000L);
        i1.setRatePerUnit(540);
        i1.setPowerGenerated(1000000L);
        i1.setInvestmentTenure(60);
        i1.setInvestmentDate(Date.valueOf("2021-11-11"));
        i1.setCoupon(couponRepository.findById(1L).get());
        i1.setListing(l1);

        Optional<Investor> investorOptional = investorRepository.findById(1L);
        Investor investor = null;
        if(investorOptional.isEmpty()) {
            log.error("Investor not found with transId 1");
        }
        else {
            investor = investorOptional.get();
            investor.addInvestment(i1);
            i1.setInvestor(investor);
        }

        l1.addInvestment(i1);

        Investment i2 = new Investment();
        i2.setAmount(200000L);
        i2.setRatePerUnit(540);
        i2.setPowerGenerated(1000000L);
        i2.setInvestmentTenure(60);
        i2.setInvestmentDate(Date.valueOf("2021-11-12"));
        i2.setCoupon(couponRepository.findById(1L).get());
        i2.setListing(l1);

        investorOptional = investorRepository.findById(2L);
        if(investorOptional.isEmpty()) {
            log.error("Investor not found with transId 1");
        }
        else {
            investor = investorOptional.get();
            investor.addInvestment(i2);
            i2.setInvestor(investor);
        }

        l1.addInvestment(i2);

        return listings;
    }
}
