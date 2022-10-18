package com.example.srot.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SecretHolder {

    @Value("${srot.security.jwt.secret}")
    private String secret;
    @Value("${srot.domain.url}")
    private String domain;

}
