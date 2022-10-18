package com.example.srot.business.service;

import com.example.srot.data.model.Investor;
import com.example.srot.data.model.Profile;
import com.example.srot.data.model.RazorpayContact;
import com.example.srot.data.repository.RazorpayContactRepository;
import com.example.srot.data.utils.HttpRequestAuthUtil;
import com.example.srot.data.utils.RazorpayAccountUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RazorpayContactService {
    @Value("${srot.rzpay.apiKey}")
    private String apiKey;

    @Value("${srot.rzpay.secretKey}")
    private String secretKey;

    private final RazorpayContactRepository razorpayContactRepository;

    @Autowired
    public RazorpayContactService(RazorpayContactRepository razorpayContactRepository) {
        this.razorpayContactRepository = razorpayContactRepository;
    }

    public RazorpayContact createContact(Profile profile) {
        HttpHeaders headers = HttpRequestAuthUtil.getAuthHeader(apiKey, secretKey);
        String data = "\"name\":\"%s\", \"email\":\"%s\", \"contact\":\"%s\", \"type\":\"%s\", " +
                "\"reference_id\":\"%s\"";
        data = String.format(data, profile.getName(), profile.getEmail(), profile.getPhoneNumber(),
                (profile instanceof Investor) ? "Investor" : "Consumer", profile.getId());

        //HttpEntity<String> entity = new HttpEntity<>(data, headers);
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.razorpay.com/v1/contacts";

        JSONObject properties = new JSONObject();
        properties.put("name", profile.getName());
        properties.put("email", profile.getEmail());
        properties.put("contact", profile.getPhoneNumber().toString());
        properties.put("type", "vendor");
                //(profile instanceof Investor) ? "Investor" : "Consumer");
        properties.put("reference_id", profile.getId().toString());

        HttpEntity<String> request = new HttpEntity<>(properties.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());

        RazorpayContact razorpayContact = RazorpayContact.builder().
                contactId(jsonObject.getString("id")).
                active(jsonObject.getBoolean("active")).
                timestamp(String.valueOf(jsonObject.getInt("created_at"))).build();

        return razorpayContactRepository.save(razorpayContact);
        //return null;
    }
}
