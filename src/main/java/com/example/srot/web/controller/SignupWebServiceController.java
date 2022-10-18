package com.example.srot.web.controller;

import com.example.srot.business.domain.*;
import com.example.srot.business.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.srot.business.domain.DefaultResponse.Status.SUCCESS;

@RestController
@RequestMapping("/signup")
public class SignupWebServiceController {

    private final SignupService service;

    @Autowired
    public SignupWebServiceController(SignupService service) {
        this.service = service;
    }

    @PostMapping("/investor")
    public ResponseEntity<DefaultResponse> investorSignup(@RequestBody NewUser newUser){
        InvestorProfile result = service.createNewInvestor(newUser);
        Map<String,InvestorProfile> data = Map.of("investor_profile",result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Investor created successfully"));
    }

    @PostMapping("/consumer")
    public ResponseEntity<DefaultResponse> consumerSignup(@RequestBody NewConsumer newUser){
        ConsumerProfile result = service.createNewConsumer(newUser);
        Map<String,ConsumerProfile> data = Map.of("consumer_profile",result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Consumer created successfully"));
    }

    @GetMapping("/verify_email")
    public ResponseEntity<DefaultResponse> verifyEmail(@RequestParam String code){
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,service.confirmEmail(code)));
    }


}
