package com.example.srot.web.controller;

import com.example.srot.business.domain.DefaultResponse;
import com.example.srot.business.domain.Msg91Response;
import com.example.srot.business.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.example.srot.business.domain.DefaultResponse.Status.SUCCESS;

@RestController
@RequestMapping("/otp")
public class OtpWebServiceController {

    private final OtpService service;

    @Autowired
    public OtpWebServiceController(OtpService service) {
        this.service = service;
    }

    @GetMapping("/generate_otp")
    public ResponseEntity<DefaultResponse> generateOtp(@RequestParam String mobile){
        Msg91Response result = service.generateOtp(mobile);
        Map<String,Msg91Response> data = Map.of("otp_api_response",result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"OTP generated successfully"));

    }
    @GetMapping("/validate_otp")
    public ResponseEntity<DefaultResponse> validateOtp(@RequestParam String mobile,
                                                     @RequestParam String otp){
        Msg91Response result = service.validateOtp(mobile,otp);
        Map<String,Msg91Response> data = Map.of("otp_api_response",result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"OTP validated successfully"));

    }

    @GetMapping("/resend_otp")
    public ResponseEntity<DefaultResponse> resentOtp(@RequestParam String mobile,
                                                   @RequestParam String type){
        Msg91Response result = service.resendOtp(mobile,type);
        Map<String,Msg91Response> data = Map.of("otp_api_response",result);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data,"OTP resent successfully"));
    }
}
