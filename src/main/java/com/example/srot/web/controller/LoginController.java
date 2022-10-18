package com.example.srot.web.controller;

import com.example.srot.business.domain.DefaultResponse;
import com.example.srot.business.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.example.srot.business.domain.DefaultResponse.Status.SUCCESS;

@RestController
@Slf4j
public class LoginController {

    @Value("${srot.domain.url}")
    private String domain;
    private final AuthenticationService service;

    @Autowired
    public LoginController(AuthenticationService service) {
        this.service = service;
    }
    @PostMapping("/login/refreshToken")
    public ResponseEntity<DefaultResponse> refreshToken(HttpServletRequest request, HttpServletResponse response){
        String accessToken = service.refreshAccessToken(request,response);
        Map<String,String> data = Map.of("access_token",accessToken);
        return ResponseEntity.ok(new DefaultResponse(SUCCESS,data, "Access token has been refreshed successfully"));
    }

}
