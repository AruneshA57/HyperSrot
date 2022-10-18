package com.example.srot.business.service;

import com.example.srot.business.domain.Msg91Response;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OtpService {

    @Value("${srot.msg91.templateId.login}")
    private String templateId;
    @Value("${srot.msg91.authKey}")
    private String authKey;
    private final RestTemplate restTemplate;

    @Autowired
    public OtpService() {
        this.restTemplate = new RestTemplateBuilder().build();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

    }

    public Msg91Response validateOtp(String mobile, String otp){
        String url =
                "https://api.msg91.com/api/v5/otp/verify?" +
                "authkey=%s" +
                "&mobile=%s" +
                "&otp=%s";
        url = String.format(url,authKey,mobile,otp);
        ResponseEntity<Msg91Response> response = restTemplate.getForEntity(url,Msg91Response.class);
        return response.getBody();
    }

    public Msg91Response generateOtp(String mobile){
        String url =
                "https://api.msg91.com/api/v5/otp?" +
                "template_id=%s" +
                "&authkey=%s" +
                "&mobile=%s" +
                "&otp_expiry=10" +
                "&otp_length=6";
        url = String.format(url,templateId,authKey,mobile);
        ResponseEntity<Msg91Response> response = restTemplate.getForEntity(url,Msg91Response.class);
        return response.getBody();

    }

    public Msg91Response resendOtp(String mobile, String type){
        String url =
                "https://api.msg91.com/api/v5/otp/retry?" +
                "authkey=%s" +
                "&mobile=%s" +
                "&retrytype=%s";
        url = String.format(url,authKey,mobile,type);
        ResponseEntity<Msg91Response> response = restTemplate.getForEntity(url,Msg91Response.class);
        return response.getBody();
    }

}
