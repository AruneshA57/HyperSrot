package com.example.srot.web.controller;

import com.example.srot.business.domain.DefaultResponse;
import com.example.srot.business.domain.InvestorReferralDetails;
import com.example.srot.business.service.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

import static com.example.srot.business.domain.DefaultResponse.Status.SUCCESS;

@RestController
@RequestMapping("/referral")
public class ReferralController {

    private final ReferralService service;

    @Autowired
    public ReferralController(ReferralService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<DefaultResponse> getReferralDetails(Principal principal){
        InvestorReferralDetails result = service.getReferralDetails();
        Map<String,InvestorReferralDetails> data = Map.of("referral_data",result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"Referral data retrieved successfully"));
    }
}
