package com.example.srot.business.service;

import com.example.srot.business.domain.*;
import com.example.srot.business.service.exceptions.*;
import com.example.srot.data.enums.KYCStatus;
import com.example.srot.data.model.*;
import com.example.srot.data.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.srot.data.enums.Role.CONSUMER;
import static com.example.srot.data.enums.Role.INVESTOR;
import static com.example.srot.data.model.EmailCode.Status.CONFIRMED;
import static com.example.srot.data.model.EmailCode.Status.EXPIRED;
import static java.time.temporal.ChronoUnit.YEARS;

@Service
@Slf4j
public class SignupService {

    @Value("${srot.investor.age}")
    private int minimumAge;
    @Value("${srot.referral.bonus}")
    private Long referralBonus;
    private final InvestorRepository investorRepository;
    private final ConsumerRepository consumerRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailCodeRepository emailRepository;
    private final ReferralRepository referralRepository;
    private final RazorpayContactService razorpayContactService;

    @Autowired
    public SignupService(InvestorRepository investorRepository, ConsumerRepository consumerRepository,
                         OtpService otpService, PasswordEncoder passwordEncoder, EmailService emailService,
                         EmailCodeRepository emailRepository, ReferralRepository referralRepository,
                         RazorpayContactService razorpayContactService) {
        this.investorRepository = investorRepository;
        this.consumerRepository = consumerRepository;
        this.otpService = otpService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.emailRepository = emailRepository;
        this.referralRepository = referralRepository;
        this.razorpayContactService = razorpayContactService;
    }

    @Transactional
    public InvestorProfile createNewInvestor(NewUser newUser) {
        validateInitialInformation(newUser);
        Optional<Investor> referrer;
        if(newUser.getReferralCode()!=null){
            referrer = investorRepository.findByReferralCode(newUser.getReferralCode());
            if (referrer.isEmpty()){
                throw new UserInfoException("Wrong referral code.");
            }
        }else referrer = Optional.empty();
        log.info("Checking for existing user information");
        Optional<Investor> existingEmailUser = investorRepository.findByEmail(newUser.getEmail());
        if(existingEmailUser.isPresent()){
            log.error("Provided e-mail already in use.");
            throw new UserInfoException("Provided e-mail already in use.");
        }
        Optional<Investor> existingNumberUser = investorRepository.findByPhoneNumber(newUser.getPhone());
        if(existingNumberUser.isPresent()){
            log.error("Provided phone number already in use");
            throw new UserInfoException("Provided phone number already in use");
        }
        if(newUser.getOtp()==null){
            log.info("No OTP provided, sending OTP to user");
            otpService.generateOtp(newUser.getPhone());
            throw new NoOtpProvidedException();
        }

        log.info("Validating DOB, password and OTP");
        LocalDate userDOB = LocalDate.parse(newUser.getDateOfBirth(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        long userAge = userDOB.until(LocalDate.now(), YEARS);
        if(userAge<minimumAge)throw new UserInfoException("User's age is under the minimum acceptable");

        Util.validatePassword(newUser.getPassword());

        Msg91Response otpResponse = otpService.validateOtp(newUser.getPhone(),newUser.getOtp());
        if(!otpResponse.getType().equals("success")){
            log.error("Provided OTP validation has failed.");
            throw new InvalidOtpException();
        }
        Investor entity = new Investor(newUser);
        entity.setRole(INVESTOR);
        setDefaultUserDetailsInfo(entity,newUser.getPassword());
        entity = investorRepository.save(entity);
        entity.setWallet(new Wallet(entity));
        entity.setReferralCode(generateReferralCode(entity));
        entity.setAadharKYCStatus(KYCStatus.PENDING);
        entity.setPanKYCStatus(KYCStatus.PENDING);

        if(referrer.isPresent()){
            entity.getWallet().setJoiningBonus(referralBonus);
            Referral referral = new Referral(referrer.get(),entity,0L);
            referralRepository.save(referral);
        }

        RazorpayContact razorpayContact = razorpayContactService.createContact(entity);
        entity.setRazorpayContact(razorpayContact);

        entity = investorRepository.save(entity);
        log.info("New investor created successfully");
        emailService.sendConfirmationEmail(entity);
        return new InvestorProfile(entity);
    }

    @Transactional(readOnly = true)
    String generateReferralCode(Investor entity) {
        LocalDate joiningDate = entity.getJoiningDate().toLocalDate();
        Pattern pattern = Pattern.compile("(?<= )[a-zA-Z]");
        Matcher matcher = pattern.matcher(entity.getName());
        StringBuilder initials = new StringBuilder(entity.getName().charAt(0) + "");
        while(matcher.find()){
            initials.append(matcher.group());
        }
        String monthDay = String.format("%02d",joiningDate.getYear()-2000) +
                          String.format("%02d",joiningDate.getMonthValue()) +
                          String.format("%02d",joiningDate.getDayOfMonth());
        String count = investorRepository.todayCount(Date.valueOf(LocalDate.now()))+"";
        return initials.toString().toUpperCase()+monthDay+count;
    }

    @Transactional
    public ConsumerProfile createNewConsumer(NewConsumer newUser) {

        validateInitialInformation(newUser);

        log.info("Checking for existing user information");
        Optional<Consumer> existingEmailUser = consumerRepository.findByEmail(newUser.getEmail());
        if(existingEmailUser.isPresent()){
            log.error("Provided e-mail already in use.");
            throw new UserInfoException("Provided e-mail already in use.");
        }
        Optional<Consumer> existingNumberUser = consumerRepository.findByPhoneNumber(newUser.getPhone());
        if(existingNumberUser.isPresent()){
            log.error("Provided phone number already in use");
            throw new UserInfoException("Provided phone number already in use");
        }
        if(newUser.getOtp()==null){
            log.info("No OTP provided, sending OTP to user");
            otpService.generateOtp(newUser.getPhone());
            throw new NoOtpProvidedException();
        }

        log.info("Validating DOB, password and OTP");
        LocalDate userDOB = LocalDate.parse(newUser.getDateOfBirth(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        long userAge = userDOB.until(LocalDate.now(), YEARS);
        if(userAge<minimumAge)throw new UserInfoException("User's age is under the minimum acceptable");

        Util.validatePassword(newUser.getPassword());

        Msg91Response otpResponse = otpService.validateOtp(newUser.getPhone(),newUser.getOtp());
        if(!otpResponse.getType().equals("success")){
            log.error("Provided OTP validation has failed.");
            throw new InvalidOtpException();
        }

        Consumer entity = new Consumer(newUser);
        entity.setRole(CONSUMER);
        setDefaultUserDetailsInfo(entity,newUser.getPassword());

        RazorpayContact razorpayContact = razorpayContactService.createContact(entity);
        entity.setRazorpayContact(razorpayContact);

        entity = consumerRepository.save(entity);
        emailService.sendConfirmationEmail(entity);
        log.info("New consumer created successfully");
        return new ConsumerProfile(entity);

    }

    @Transactional
    public String confirmEmail(String code) {
        EmailCode confirmationCode = emailRepository.findByCode(code)
                .orElseThrow(()->new NotFoundException("No such code was found"));
        if(confirmationCode.getStatus().equals(CONFIRMED)){
            return "Email has already been confirmed";//TODO update this to comply to system standardization
        }
        if (confirmationCode.getStatus().equals(EXPIRED)){
            return "This code has already expired"; //TODO update this to comply to system standardization
        }
        confirmationCode.setStatus(CONFIRMED);
        confirmationCode = emailRepository.save(confirmationCode);
        Optional<? extends Profile> userOptional = consumerRepository.findById(confirmationCode.getUserId());
        if(userOptional.isEmpty()) userOptional = investorRepository.findById(confirmationCode.getUserId());
        Profile user = userOptional.get();
        user.setEmailConfirmed(true);
        if(user instanceof Investor) investorRepository.save((Investor)user);
        if(user instanceof Consumer) consumerRepository.save((Consumer)user);
        return "Email confirmed successfully";
    }

    private void setDefaultUserDetailsInfo(Profile entity, String password) {
        entity.setPassword(passwordEncoder.encode(password));
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialNonExpired(true);
        entity.setEnabled(true);
        entity.setEmailConfirmed(false);
    }

    private void validateInitialInformation(NewUser newUser) {
        if(newUser.getName()==null || newUser.getName().isBlank() ||
                newUser.getPhone()==null || newUser.getPhone().isBlank() ||
                newUser.getEmail()==null || newUser.getEmail().isBlank()){
            throw new UserInfoException("Name, phone number or email is missing");
        }
    }
}
