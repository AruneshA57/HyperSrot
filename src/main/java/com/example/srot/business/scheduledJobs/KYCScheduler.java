package com.example.srot.business.scheduledJobs;

import com.example.srot.data.enums.KYCStatus;
import com.example.srot.data.model.BankKYC;
import com.example.srot.data.model.Profile;
import com.example.srot.data.model.RazorpayDebitTransaction;
import com.example.srot.data.model.Wallet;
import com.example.srot.data.repository.BankKYCRepository;
import com.example.srot.data.repository.WalletRepository;
import com.example.srot.data.utils.DateUtil;
import com.example.srot.data.utils.HttpRequestAuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

@Component
@Slf4j
public class KYCScheduler {
    @Value("${srot.rzpay.apiKey}")
    private String apiKey;

    @Value("${srot.rzpay.secretKey}")
    private String secretKey;

    @Value("${srot.rzpay.verificationAccount}")
    private String verificationAccountNumber;

    @Value("${srot.rzpay.payoutAccount}")
    private String payoutAccountNumber;

    private final BankKYCRepository bankKYCRepository;
    private final WalletRepository walletRepository;

    public KYCScheduler(BankKYCRepository bankKYCRepository, WalletRepository walletRepository) {
        this.bankKYCRepository = bankKYCRepository;
        this.walletRepository = walletRepository;
    }

    private void updateKYCStatus(BankKYC bankKYC) {
        HttpHeaders headers = HttpRequestAuthUtil.getAuthHeader(apiKey, secretKey);
        String url = "https://api.razorpay.com/v1/fund_accounts/validations/" + bankKYC.getValidationId();
        HttpEntity<String> request = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());
        String jsonStatus = jsonObject.getString("status");
        if(Objects.equals(jsonStatus, "created")) bankKYC.setStatus(KYCStatus.CREATED);
        else if(Objects.equals(jsonStatus, "failed")) bankKYC.setStatus(KYCStatus.FAILED);
        else if(Objects.equals(jsonStatus, "completed")) bankKYC.setStatus(KYCStatus.COMPLETE);
        bankKYC.setTimestamp(String.valueOf(jsonObject.getInt("created_at")));

        if(bankKYC.getStatus().equals(KYCStatus.COMPLETE)) {
            RazorpayDebitTransaction razorpayDebitTransaction = new RazorpayDebitTransaction();
            razorpayDebitTransaction.setParticulars("Bank KYC");
            razorpayDebitTransaction.setAmount(bankKYC.getAmount());
            razorpayDebitTransaction.setTimestamp(new Date(Long.parseLong(bankKYC.getTimestamp())));
            razorpayDebitTransaction.setStatus("SUCCESS");

            bankKYC.setRazorpayDebitTransaction(razorpayDebitTransaction);

            Profile profile = (bankKYC.getInvestor() != null) ? bankKYC.getInvestor() : bankKYC.getConsumer();
            if(profile == null) {
                log.error("No profile linked to KYC id: " + bankKYC.getId());
                return;
            }

            Wallet wallet = profile.getWallet();
            if(wallet == null) {
                log.info("Wallet not found for profile id: " + profile.getId());
                return;
            }

            wallet.setWalletBalance(wallet.getWalletBalance() - bankKYC.getAmount());
            profile.setWallet(walletRepository.save(wallet));
        }

        bankKYCRepository.save(bankKYC);
        log.info("KYC status updated to " + bankKYC.getStatus() + " for id: " + bankKYC.getId());
    }

    @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
    public void checkAndUpdateKYCStatus() {
        Iterable<BankKYC> bankKYCIterable = bankKYCRepository.findAllByStatus(KYCStatus.CREATED);
        bankKYCIterable.forEach(bankKYC -> {
            Long days = DateUtil.getDaysInBetween(new Date(Long.parseLong(bankKYC.getTimestamp()) * 1000L).
                            toLocalDate(), LocalDate.now());
            if(days <= 7) {
                updateKYCStatus(bankKYC);
            }
            else {
                bankKYC.setStatus(KYCStatus.FAILED);
                bankKYCRepository.save(bankKYC);
                log.info("KYC Failed for id: " + bankKYC.getId());
            }
        });
    }
}
