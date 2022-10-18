package com.example.srot.data.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HttpRequestAuthUtil {

    public static HttpHeaders getAuthHeader(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}
