package com.example.srot.business.service;

import com.example.srot.data.enums.KYCStatus;
import com.example.srot.data.model.*;
import com.example.srot.data.repository.BankKYCRepository;
import com.example.srot.data.repository.RazorpayFundAccountRepository;
import com.example.srot.data.repository.WalletRepository;
import com.example.srot.data.utils.HttpRequestAuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.Objects;

@Slf4j
@Service
public class RazorpayFundAccountService {
    @Value("${srot.rzpay.apiKey}")
    private String apiKey;

    @Value("${srot.rzpay.secretKey}")
    private String secretKey;

    @Value("${srot.rzpay.verificationAccount}")
    private String verificationAccountNumber;

    @Value("${srot.rzpay.payoutAccount}")
    private String payoutAccountNumber;

    @Value("${srot.kycStatus.flag}")
    private String razorPayKYCValidationFlag ;

    private final RazorpayFundAccountRepository razorpayFundAccountRepository;
    private HttpHeaders headers;
    private final WalletRepository walletRepository;
    private final BankKYCRepository bankKYCRepository;
    private final RazorpayContactService razorpayContactService;

    public RazorpayFundAccountService(RazorpayFundAccountRepository razorpayFundAccountRepository,
                                      WalletRepository walletRepository,
                                      BankKYCRepository bankKYCRepository,
                                      RazorpayContactService razorpayContactService) {
        this.razorpayFundAccountRepository = razorpayFundAccountRepository;
        this.walletRepository = walletRepository;
        this.bankKYCRepository = bankKYCRepository;
        this.razorpayContactService = razorpayContactService;
    }

    private void setHttpAuthHeader() {
        this.headers = HttpRequestAuthUtil.getAuthHeader(apiKey, secretKey);
    }

    private void createFundAccount(Profile profile) {
        if(profile.getRazorpayContact() == null) {
            profile.setRazorpayContact(razorpayContactService.createContact(profile));
        }
//        String data = "\"contact_id\":\"%s\", \"account_type\":\"%s\", \"bank_account\":" +
//                "{\"name\":\"%s\", \"ifsc\":\"%s\", \"account_number\":\"%s\"}";
//        data = String.format(data, profile.getRazorpayContact().getId().toString(), "bank_account",
//                profile.getBankAccountName(), profile.getBankIfsc(), profile.getBankAccountNumber());

        JSONObject bank = new JSONObject();
        bank.put("name", profile.getBankAccountName());
        bank.put("ifsc", profile.getBankIfsc());
        bank.put("account_number", profile.getBankAccountNumber());

        JSONObject properties = new JSONObject();
        properties.put("contact_id", profile.getRazorpayContact().getContactId());
        properties.put("account_type", "bank_account");
        properties.put("bank_account", bank);

        String url = "https://api.razorpay.com/v1/fund_accounts";

        HttpEntity<String> request = new HttpEntity<>(properties.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());

        RazorpayFundAccount razorpayFundAccount = RazorpayFundAccount.builder().
                fundId(jsonObject.getString("id")).
                active(jsonObject.getBoolean("active")).
                timestamp(String.valueOf(jsonObject.getInt("created_at"))).build();

        JSONObject bankAccount = jsonObject.getJSONObject("bank_account");

        profile.setBankName(bankAccount.getString("bank_name"));
        profile.setRazorpayFundAccount(razorpayFundAccountRepository.save(razorpayFundAccount));
    }

    private void validateAccount(Profile profile) {
//        String data = "\"account_number\":\"%s\", \"fund_account\":{\"transId\":\"%s\"}, \"amount\":\"%s\", " +
//                "\"currency\":\"%s\"";
//        data = String.format(data, verificationAccountNumber, profile.getRazorpayFundAccount().getId(), "100", "INR");

        BankKYC bankKYC = profile.getBankKYC();
        if(bankKYC == null) {
            bankKYC = new BankKYC();
            if(profile instanceof Investor) {
                bankKYC.setInvestor((Investor) profile);
            }
            else {
                bankKYC.setConsumer((Consumer) profile);
            }
        }

        if(Objects.equals(bankKYC.getStatus(),KYCStatus.COMPLETE) && Objects.equals(razorPayKYCValidationFlag, "testing")){
            return;
        }

        JSONObject fund = new JSONObject();
        fund.put("id", profile.getRazorpayFundAccount().getFundId());

        JSONObject properties = new JSONObject();
        properties.put("account_number", verificationAccountNumber);
        properties.put("fund_account", fund);
        properties.put("amount", "100");
        properties.put("currency", "INR");

        String url = "https://api.razorpay.com/v1/fund_accounts/validations";

        HttpEntity<String> request = new HttpEntity<>(properties.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());


        bankKYC.setValidationId(jsonObject.getString("id"));
        if(Objects.equals(razorPayKYCValidationFlag, "testing") && Objects.equals(jsonObject.getString("status"), "created")){
            bankKYC.setStatus(KYCStatus.COMPLETE);
        }
        else{
            String jsonStatus = jsonObject.getString("status");
            if(Objects.equals(jsonStatus, "failed")) bankKYC.setStatus(KYCStatus.FAILED);
            else if(Objects.equals(jsonStatus, "completed")) bankKYC.setStatus(KYCStatus.COMPLETE);
            else bankKYC.setStatus(KYCStatus.PENDING);
        }
        bankKYC.setAmount((long) jsonObject.getInt("amount"));
        bankKYC.setCurrency(jsonObject.getString("currency"));
        bankKYC.setTimestamp(String.valueOf(jsonObject.getInt("created_at")));

        if(bankKYC.getStatus().equals(KYCStatus.COMPLETE)) {
            RazorpayDebitTransaction razorpayDebitTransaction = new RazorpayDebitTransaction();
            razorpayDebitTransaction.setParticulars("Bank KYC");
            razorpayDebitTransaction.setAmount(bankKYC.getAmount());
            razorpayDebitTransaction.setTimestamp(new Date(Long.parseLong(bankKYC.getTimestamp())));
            razorpayDebitTransaction.setStatus("SUCCESS");

            bankKYC.setRazorpayDebitTransaction(razorpayDebitTransaction);

            Wallet wallet = profile.getWallet();
            wallet.setWalletBalance(wallet.getWalletBalance() - bankKYC.getAmount());
            profile.setWallet(walletRepository.save(wallet));
        }

        profile.setBankKYC(bankKYCRepository.save(bankKYC));
    }

    public void performBankKYC(Profile profile) {
        if(headers == null) {
            setHttpAuthHeader();
        }
        if(profile.getRazorpayFundAccount() == null) {
            log.info("Creating fund account for user: " + profile.getId());
            createFundAccount(profile);
        }
        else {
            log.info("Fund account already exists for user: " + profile.getId());
        }

        validateAccount(profile);
    }
}
