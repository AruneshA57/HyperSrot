package com.example.srot.web.controller;

import com.example.srot.business.domain.*;
import com.example.srot.business.dto.BankKycDto;
import com.example.srot.business.dto.InvestDto;
import com.example.srot.business.service.AmazonS3Service;
import com.example.srot.business.service.InvestorService;
import com.example.srot.business.service.exceptions.UrlEncodingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static com.example.srot.business.domain.DefaultResponse.Status.SUCCESS;

@Slf4j
@RestController
@RequestMapping("/investor")
public class InvestorWebController {
    ApplicationContext

    private final AmazonS3Service amazonS3Service;
    private final InvestorService investorService;

    @Autowired
    public InvestorWebController(AmazonS3Service amazonS3Service, InvestorService investorService) {
        this.amazonS3Service = amazonS3Service;
        this.investorService = investorService;
    }


    @GetMapping("/questionnaire")
    public ResponseEntity<DefaultResponse> getQuestionnaire(){
        QuestionnaireDTO result = investorService.getQuestionnaire();
        Map<String,QuestionnaireDTO> data = Map.of("questionnaire_data",result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Questionnaire data retrieved successfully"));
    }

    @GetMapping("/portfolio")
    public ResponseEntity<DefaultResponse> showPortfolio() {
        InvestorPortfolio result = investorService.getInvestments();
        Map<String,InvestorPortfolio> data = Map.of("investor_portfolio",result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Portfolio data retrieved successfully"));
    }

    @GetMapping("/profile")
    public ResponseEntity<DefaultResponse> getInvestorProfile() {
        InvestorProfile result = investorService.getInvestorProfile();
        Map<String,InvestorProfile> data = Map.of("investor_profile",result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Portfolio data retrieved successfully"));
    }

    @GetMapping("/download/documents")
    public ResponseEntity<DefaultResponse> downloadDocuments(@RequestParam String doc) {
        List<Byte> result = amazonS3Service.downloadDocuments(doc);
        Map<String,List<Byte>> data = Map.of(doc,result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Document data retrieved successfully"));
    }
    @PostMapping("/asset/invest")
    public ResponseEntity<DefaultResponse> addInvestment(@RequestBody InvestDto investDto) {

//        Coupon coupon = couponService.validateCoupon(couponCode);
//        if(coupon == null) {
//            log.error("Invalid coupon");
//            return null;
//        }
        InvestmentDisplay result = investorService.addInvestment(investDto);
        Map<String,InvestmentDisplay> data = Map.of("investment",result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Investment added successfully"));
    }

    @PostMapping("/questionnaire")
    public ResponseEntity<DefaultResponse> saveQuestionnaire(@RequestBody QuestionnaireDTO dto){
        QuestionnaireDTO questionnaireDTO = investorService.saveQuestionnaire(dto,"INSERT");
        Map<String,QuestionnaireDTO> data = Map.of("questionnaire_data",questionnaireDTO);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Questionnaire saved successfully"));
    }

    @PostMapping("/upload/documents")
    public ResponseEntity<DefaultResponse> uploadDocuments(
             @RequestPart(value = "pan") MultipartFile pan,
             @RequestPart(value = "aadhar_front") MultipartFile aadharFront,
             @RequestPart(value = "aadhar_back") MultipartFile aadharBack) {
        amazonS3Service.uploadDocuments(pan, "PAN");
        amazonS3Service.uploadDocuments(aadharFront, "Aadhar_Front");
        amazonS3Service.uploadDocuments(aadharBack, "Aadhar_Back");

        return ResponseEntity.ok(new DefaultResponse(SUCCESS, "Documents uploaded Successfully"));
    }

    @PostMapping("/profile/bank_kyc")
    public ResponseEntity<DefaultResponse> bankKYC(@RequestBody BankKycDto bankKycDto) {
        BankDisplay result = investorService.bankKYC(bankKycDto);
        Map<String,BankDisplay> data = Map.of("bank_kyc",result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Bank KYC saved successfully"));
    }

    @PostMapping("/upload/contract")
    public ResponseEntity<DefaultResponse> uploadContract(
             @RequestPart MultipartFile file,@RequestParam String doc) {
        amazonS3Service.uploadDocuments(file,doc);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS, "Contract uploaded Successfully"));
    }

    @PutMapping("/update")
    public ResponseEntity<DefaultResponse> updatePersonal(@RequestBody NewUser dto){
        InvestorProfile result = investorService.updateInvestor(dto);
        Map<String,InvestorProfile> data = Map.of("investor_data",result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Investor updated successfully"));
    }

    @PutMapping("/questionnaire")
    public ResponseEntity<DefaultResponse> updateQuestionnaire(@RequestBody QuestionnaireDTO dto){
        QuestionnaireDTO result = investorService.saveQuestionnaire(dto,"UPDATE");
        Map<String,QuestionnaireDTO> data = Map.of("questionnaire_data",result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Questionnaire updated successfully"));
    }

    @PutMapping("/forget_password")
    public ResponseEntity<DefaultResponse> forgetPassword(@RequestBody Map<String, String> userDetails) throws UnsupportedEncodingException {
        investorService.forgetPassword(userDetails);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,"Investor Password has been reset successfully"));
    }
    @PutMapping("/change_password")
    public ResponseEntity<DefaultResponse> changePassword(@RequestBody Map<String, String> userDetails){
        investorService.changePassword(userDetails);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,"Investor Password has been updated successfully"));
    }

}
