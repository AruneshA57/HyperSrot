package com.example.srot.business.service;

import com.example.srot.business.converter.InvestorToInvestorPortfolioConverter;
import com.example.srot.business.converter.InvestorToInvestorProfileConverter;
import com.example.srot.business.domain.*;
import com.example.srot.business.dto.BankKycDto;
import com.example.srot.business.dto.InvestDto;
import com.example.srot.business.service.exceptions.*;
import com.example.srot.data.exceptions.InvestorNotFoundException;
import com.example.srot.data.exceptions.WalletBalanceInsufficientException;
import com.example.srot.data.model.*;
import com.example.srot.data.repository.*;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.srot.data.model.EmailCode.Status.PENDING;
import static java.time.temporal.ChronoUnit.YEARS;

@Slf4j
@Service
public class InvestorService {

    @Value("${srot.investor.age}")
    private Integer minimumAge;
    @Value("${srot.domain.url}")
    private String domain;
    private final Long questionnaireBonus=100000L;
    private final InvestorRepository investorRepository;
    private final WalletRepository walletRepository;
    private final EmailService emailService;
    private final AuthenticationService authenticationService;
    private final CouponRepository couponRepository;
    private final InvestorCouponRepository investorCouponRepository;
    private final InvestorQuestionnaireRepository questionnaireRepository;
    private final ReferralRepository referralRepository;
    private final RazorpayFundAccountService razorpayFundAccountService;
    private final ReferralCreditTransactionRepository referralCreditTransactionRepository;
    private final InvestorToInvestorProfileConverter investorToInvestorProfileConverter;
    private final InvestorToInvestorPortfolioConverter investorToInvestorPortfolioConverter;
    private final DebitInvestmentTransactionRepository debitInvestmentTransactionRepository;
    private final ListingService listingService;
    private final EmailCodeRepository emailCodeRepository;
    private final PasswordEncoder encoder;
    private final OtpService otpService;

    public InvestorService(InvestorRepository investorRepository,
                           WalletRepository walletRepository,
                           AuthenticationService authenticationService, ReferralRepository referralRepository,
                           RazorpayFundAccountService razorpayFundAccountService,
                           ReferralCreditTransactionRepository referralCreditTransactionRepository,
                           InvestorToInvestorProfileConverter investorToInvestorProfileConverter,
                           InvestorToInvestorPortfolioConverter investorToInvestorPortfolioConverter,
                           DebitInvestmentTransactionRepository debitInvestmentTransactionRepository,
                           EmailService emailService,
                           CouponRepository couponRepository,
                           InvestorCouponRepository investorCouponRepository,
                           InvestorQuestionnaireRepository questionnaireRepository,
                           ListingService listingService, EmailCodeRepository emailCodeRepository, ConsumerRepository consumerRepository, PasswordEncoder encoder, OtpService otpService) {
        this.investorRepository = investorRepository;
        this.walletRepository = walletRepository;
        this.authenticationService = authenticationService;
        this.emailService = emailService;
        this.couponRepository = couponRepository;
        this.investorCouponRepository = investorCouponRepository;
        this.questionnaireRepository = questionnaireRepository;
        this.referralRepository = referralRepository;
        this.listingService = listingService;
        this.razorpayFundAccountService = razorpayFundAccountService;
        this.referralCreditTransactionRepository = referralCreditTransactionRepository;
        this.investorToInvestorProfileConverter = investorToInvestorProfileConverter;
        this.investorToInvestorPortfolioConverter = investorToInvestorPortfolioConverter;
        this.debitInvestmentTransactionRepository = debitInvestmentTransactionRepository;
        this.emailCodeRepository = emailCodeRepository;
        this.encoder = encoder;
        this.otpService = otpService;
    }

    public InvestorProfile getInvestorProfile() {
        Investor investor = authenticationService.findAuthenticatedInvestor();
        return new InvestorProfile(investor);
    }

    public InvestorProfile getInvestorProfile(Long id) {
        Optional<Investor> investorOptional = investorRepository.findById(id);

        if(investorOptional.isEmpty()) {
            System.out.println("No investor found with transId: " + id);
            return null;
        }

        Investor investor = investorOptional.get();

        return investorToInvestorProfileConverter.convert(investor);
    }

    private DebitInvestmentTransaction saveDebitTransaction(Long amount, Wallet wallet, Investment investment,
                                                            String particulars) {
        DebitInvestmentTransaction transaction = new DebitInvestmentTransaction();
        transaction.setAmount(amount);
        transaction.setParticulars("Debit from " + particulars);
        transaction.setTimestamp(new Date(Calendar.getInstance().getTime().getTime()));
        transaction.setStatus("COMPLETE");
        transaction.setWallet(wallet);
        transaction.setInvestment(investment);
        return debitInvestmentTransactionRepository.save(transaction);
    }

    private void processReferralBonus(Investor investor) {
        Referral referral = referralRepository.findByReferee(investor).orElseThrow();
        Investor referrer = referral.getReferrer();
        Wallet referrerWallet = referrer.getWallet();
        referrerWallet.addReferralBonus(referral.getReferralBonus());

        ReferralCreditTransaction transaction = new ReferralCreditTransaction();
        transaction.setAmount(referral.getReferralBonus());
        transaction.setParticulars("Bonus for referring " + investor.getName());
        transaction.setTimestamp(new Date(Calendar.getInstance().getTime().getTime()));
        transaction.setWallet(referrer.getWallet());
        transaction.setStatus("COMPLETE");

        referralCreditTransactionRepository.save(transaction);
        walletRepository.save(referrerWallet);
        investorRepository.save(referrer);
    }

    private void calculateReturns(Investment investment) {
        long investedAmount = investment.getAmount();
        InvestorReturns investorReturns = investment.getListing().getInvestorReturns();
        InvestorRate investorRate = investment.getListing().
                getInvestorRateByTenure(Long.valueOf(investment.getInvestmentTenure()));

        double numOfKw = (double) investedAmount / investorReturns.getCostPerKW();
        double amountPerMonth = investorRate.getInvestorRate() * numOfKw * investorReturns.getUnitsPerDayPerKW() * 30;

        long tds = (long) Math.ceil(amountPerMonth * investorReturns.getTds() / 100.0);
        long returns = (long) (amountPerMonth - tds);

        for(int i = 0; i < investment.getInvestmentTenure(); ++i) {
            if(i % 11 == 0) {
                long depreciation = (long) Math.ceil(amountPerMonth * investorReturns.getPowerDepreciation() / 100.0);
                amountPerMonth -= depreciation;
            }

            tds = (long) Math.ceil(amountPerMonth * investorReturns.getTds() / 100.0);
            returns += amountPerMonth - tds;
        }

        long finalPayout = investedAmount;
        for(int i = 0; i < (investment.getInvestmentTenure() / 12); ++i) {
            finalPayout -= (investorReturns.getValueDepreciationPerYear() / 100) * finalPayout;
        }

        investment.setYield((((returns - investedAmount) + (finalPayout - investedAmount)) * 12 * 100) /
                investment.getInvestmentTenure());
    }

    public InvestmentDisplay addInvestment(InvestDto investDto) {
        Investor investor = authenticationService.findAuthenticatedInvestor();

        Listing listing = listingService.validateAndGetListing(investDto.getAssetId(), investDto.getAmount());

        Wallet wallet = investor.getWallet();
        if(wallet.getTotalBalance() < investDto.getAmount()) {
            log.error("Wallet balance is insufficient");
            throw new WalletBalanceInsufficientException(wallet.getWalletBalance().toString());
        }

        //legal document signing
        listingService.generateDoc(investDto.getAmount(), Long.valueOf(investDto.getTenure()), listing.getId());

        //debit from wallet
        //Debit priority order: Joining Bonus > Referral Bonus > Wallet Balance
        Long joiningBonusDebit = wallet.getJoiningBonus(), referralBonusDebit = wallet.getReferralBonus(),
                walletDebit = wallet.getWalletBalance();

        if(wallet.getJoiningBonus() >= investDto.getAmount()) {
            joiningBonusDebit = investDto.getAmount();
            wallet.setJoiningBonus(wallet.getJoiningBonus() - joiningBonusDebit);
        }
        else {
            if(wallet.getReferralBonus() >= (investDto.getAmount() + joiningBonusDebit)) {
                referralBonusDebit = investDto.getAmount() - joiningBonusDebit;
                wallet.setReferralBonus(wallet.getReferralBonus() - referralBonusDebit);
            }
            else {
                walletDebit = investDto.getAmount() - joiningBonusDebit - referralBonusDebit;
                wallet.setWalletBalance(wallet.getWalletBalance() - walletDebit);
                wallet.setReferralBonus(0L);
            }
            wallet.setJoiningBonus(0L);
        }

        Investment investment = Investment.builder().investor(investor)
                .investmentTenure(investDto.getTenure())
                .listing(listing)
                .investmentDate(Date.valueOf(LocalDate.now()))
                .amount(investDto.getAmount())
                .build();

        calculateReturns(investment);

        listing.addInvestment(investment);
        listing = listingService.save(listing);

        if(joiningBonusDebit > 0) {
            wallet.addDebitInvestmentTransaction(saveDebitTransaction(joiningBonusDebit, wallet, investment,
                    "Joining Bonus"));
        }
        if(referralBonusDebit > 0) {
            wallet.addDebitInvestmentTransaction(saveDebitTransaction(referralBonusDebit, wallet, investment,
                    "Referral Bonus"));
        }
        if(walletDebit > 0) {
            wallet.addDebitInvestmentTransaction(saveDebitTransaction(walletDebit, wallet, investment,
                    "Wallet"));
        }

        wallet = walletRepository.save(wallet);

        investor.addInvestment(investment);
        investor = investorRepository.save(investor);

        processReferralBonus(investor);

        return new InvestmentDisplay(investment);
    }

    public InvestorPortfolio getInvestments() {
        Investor investor = authenticationService.findAuthenticatedInvestor();
        return investorToInvestorPortfolioConverter.convert(investor);
    }
    public InvestorPortfolio getInvestments(Long investorId) {
        return new InvestorPortfolio(investorRepository.findById(investorId).
                orElseThrow(() -> new InvestorNotFoundException(investorId)));
    }

    public QuestionnaireDTO saveQuestionnaire(QuestionnaireDTO dto, String operation) {

        Investor investor = authenticationService.findAuthenticatedInvestor();
        try{
            //This tries to find a questionnaire linked to the current Investor
            //If it finds any, it proceeds without error and throws a new exception
            //in case we are trying to insert a new questionnaire from the POST endpoint.
            //or sets the dto to use the id from the questionnaire found if we are hitting
            //the PUT endpoint.
            //If it doesn't find any questionnaire, then we ignore the NotFoundException
            //and proceed with the method.
            QuestionnaireDTO temp = getQuestionnaire();
            if(operation.equals("INSERT")) throw new QuestionnaireException("Investor questionnaire has already been created");
            else dto.setId(temp.getId());
        }catch(NotFoundException ex){
            if(operation.equals("UPDATE")) throw new QuestionnaireException("Investor haven't answered the questionnaire yet ");
        }

        InvestorQuestionnaire questionnaire = new InvestorQuestionnaire(investor,dto);
        EmailTemplate template = new EmailTemplate();
        Map<String,String> templateVariables = new HashMap<>();
        Coupon coupon = couponRepository.findCouponByCouponCode("EARLY_VR_2022").get();
        InvestorCoupon investorCoupon = new InvestorCoupon(investor,coupon,false);
        investor.getWallet().setQuestionnaireBonus(questionnaireBonus);
        investorRepository.save(investor);

        questionnaire = questionnaireRepository.save(questionnaire);
        investorCouponRepository.save(investorCoupon);

        if(dto.isWebinar()){
            template.setTemplateName("Webinar-Invitation_EA-Coupon-Code");
            templateVariables.put("link","http://Webinarlink");
        }else template.setTemplateName("EA-Coupon-Code");
        templateVariables.put("code",coupon.getCouponCode());
        template.setTemplateVariables(templateVariables);
        template.setDestination(investor.getEmail());
        emailService.sendTemplatedEmail(template);
        return new QuestionnaireDTO(questionnaire);
    }

    public QuestionnaireDTO getQuestionnaire() {
        Investor investor = authenticationService.findAuthenticatedInvestor();
        InvestorQuestionnaire questionnaire = questionnaireRepository.findByInvestor(investor).orElseThrow(
                ()-> new NotFoundException("Investor hasn't submitted questionnaire yet")
        );
        return new QuestionnaireDTO(questionnaire);
    }

    public InvestorProfile updateInvestor(NewUser dto) {
        Investor entity = authenticationService.findAuthenticatedInvestor();
        LocalDate temp = LocalDate.parse(dto.getDateOfBirth(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        long userAge = temp.until(LocalDate.now(), YEARS);
        if(userAge<minimumAge)throw new UserInfoException("User's age is under the minimum acceptable");
        entity.updateProfileInfo(dto);
        entity = investorRepository.save(entity);
        log.info("Investor updated successfully");
        return new InvestorProfile(entity);

    }

    @Transactional
    public BankDisplay bankKYC(BankKycDto bankKycDto) {
        Investor investor = authenticationService.findAuthenticatedInvestor();

        investor.setBankIfsc(bankKycDto.getIfsc());
        investor.setBankAccountNumber(bankKycDto.getAccountNumber());
        investor.setBankAccountName(bankKycDto.getAccountName());

        razorpayFundAccountService.performBankKYC(investor);
        investor = investorRepository.save(investor);

        return BankDisplay.builder().
                id(investor.getId().toString()).
                bankName(investor.getBankName()).
                accountNumber(investor.getBankAccountNumber()).
                ifsc(investor.getBankIfsc()).
                kycStatus(investor.getBankKYC().getStatus().name()).build();
    }

    public void forgetPassword(Map<String, String> userDetails) throws UnsupportedEncodingException {
        //TODO This method can and probably should be transferred to a service
        //responsible for handling both consumer and investor common

        //Step 1
        if(!userDetails.containsKey("code")) {
            String username = "";

            if (userDetails.containsKey("credentials")) username = userDetails.get("credentials");

            Profile user = (Profile) authenticationService.loadUserByUsername(username);

            String code = RandomString.make(64);
            EmailCode codeEntity = new EmailCode(user.getId(), PENDING, code);

            emailCodeRepository.save(codeEntity);

            String requestParams = "code=" + code + "&credentials=" + username;
            String link = domain + "reset?" + encodeValue(requestParams);

            Map<String, String> templateVariables = Map.of("link", link);
            EmailTemplate template = new EmailTemplate(user.getEmail(), "Srot-Forget-Password", templateVariables);
            emailService.sendTemplatedEmail(template);
            //This exception is used to change the flow and does not mean an error
            throw new ForgetPasswordException("Email has been sent to user");
        }
        //Step 2
        EmailCode code = emailCodeRepository.findByCode(userDetails.get("code")).orElseThrow(
                ()-> new NotFoundException("Specified code was not found")
        );
        Investor user = investorRepository.findById(code.getUserId()).get();
        boolean tfa = user.getTwoFactorAuthentication();
        if(!tfa){
            if(userDetails.containsKey("password")){
                String password = userDetails.get("password");
                if (encoder.matches(password,user.getPassword())) throw new UserInfoException("New password can't be the same as the old one");
                Util.validatePassword(userDetails.get("password"));
                user.setPassword(encoder.encode(password));
                investorRepository.save(user);
                emailCodeRepository.delete(code);
                return;
            }else throw new UserInfoException("New password was not provided");

        }

        if(!userDetails.containsKey("otp")){
            otpService.generateOtp(user.getPhoneNumber());
            throw new ForgetPasswordException("Otp has been sent to user");
        }

        //Step 3
        Msg91Response otpResponse = otpService.validateOtp(user.getPhoneNumber(),userDetails.get("otp"));

        if (otpResponse.getType().equals("success") && userDetails.containsKey("otp") && userDetails.containsKey("password")) {
            String password = userDetails.get("password");
            if (encoder.matches(password,user.getPassword())) throw new UserInfoException("New password can't be the same as the old one");
            Util.validatePassword(userDetails.get("password"));
            user.setPassword(encoder.encode(password));
            investorRepository.save(user);
            emailCodeRepository.delete(code);
        }else throw new InvalidOtpException();

    }

    @Transactional
    public void changePassword(Map<String,String> userDetails){

        Investor user = authenticationService.findAuthenticatedInvestor();

        if(!userDetails.containsKey("current_password")) throw new UserInfoException("Current password was not provided");
        if(!userDetails.containsKey("new_password"))throw new UserInfoException("New password was not provided");

        String currentPassword = userDetails.get("current_password");

        if(!encoder.matches(currentPassword,user.getPassword())) throw new UserInfoException("Invalid current password");
        if(userDetails.get("new_password").equals(userDetails.get("current_password"))) throw new UserInfoException("New password can't be the same as the old one");

        Util.validatePassword(userDetails.get("new_password"));
        user.setPassword(encoder.encode(userDetails.get("new_password")));
        investorRepository.save(user);

    }

    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

}