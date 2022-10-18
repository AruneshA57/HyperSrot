package com.example.srot.business.service;

import com.example.srot.business.domain.InvestorReferralDetails;
import com.example.srot.data.model.Investor;
import com.example.srot.data.model.Referral;
import com.example.srot.data.model.Wallet;
import com.example.srot.data.repository.InvestorRepository;
import com.example.srot.data.repository.ReferralRepository;
import com.example.srot.data.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReferralService {

    @Value("${srot.referral.bonus}")
    private Long referralBonus;
    private final InvestorRepository investorRepository;
    private final ReferralRepository referralRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public ReferralService(InvestorRepository investorRepository, ReferralRepository referralRepository, WalletRepository walletRepository) {
        this.investorRepository = investorRepository;
        this.referralRepository = referralRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional(readOnly = true)
    public InvestorReferralDetails getReferralDetails(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Investor entity = investorRepository.findByEmail(currentUserName).get();
        Wallet wallet = entity.getWallet();
        InvestorReferralDetails response = new InvestorReferralDetails(entity.getReferralCode(),wallet.getJoiningBonus());
        List<Referral> friendsJoined = referralRepository.findByReferrer(entity);

        if (friendsJoined.isEmpty())return response;

        response.setJoiningBonus(wallet.getJoiningBonus()/100.0);
        response.setFriendsJoined(friendsJoined.size());

        List<Referral> friendsInvested = friendsJoined
                                            .stream()
                                            .filter(e -> !e.getReferee().getInvestments().isEmpty())
                                            .collect(Collectors.toList());

        response.setFriendsInvested(friendsInvested.size());
        double[] totalReferralBonus = {0};
        friendsInvested.forEach(e-> totalReferralBonus[0]+=e.getReferralBonus());
        response.setReferralBonus(totalReferralBonus[0]/100);
        response.setAvailableReferralBonus(wallet.getReferralBonus()/100.0);
        return response;

    }

    @Transactional
    public void updateReferral(Investor referee){
        if(referee.getJoiningCode()==null){
            log.info("Investor has no referrer. No updates made");
            return;
        }
        if(referee.getInvestments().size()>1){
            log.info("Friend has already invested before. No updates made");
            return;
        }
        Referral referral = referralRepository.findByReferee(referee).get();
        referral.setReferralBonus(referralBonus);
        referralRepository.save(referral);
        Wallet referrerWallet = referral.getReferrer().getWallet();
        referrerWallet.setReferralBonus(referrerWallet.getReferralBonus()+referralBonus);
        walletRepository.save(referrerWallet);
        log.info(String.format("Updated successfully. Added %.2f to referrer bonus",(referralBonus/100.0)));
    }


}
