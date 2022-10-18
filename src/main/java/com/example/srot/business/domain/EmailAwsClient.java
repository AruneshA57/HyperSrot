package com.example.srot.business.domain;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import static com.example.srot.business.domain.DefaultResponse.Status.SUCCESS;

@Getter
@Setter
@Component
@Slf4j
public class EmailAwsClient{

    private final String from = "Srot - Grow your wealth <info@srot.io>";
    private final SendTemplatedEmailRequest emailRequest = new SendTemplatedEmailRequest();
    private final AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
            .withRegion(Regions.AP_SOUTH_1).build();

    public EmailAwsClient(){
        emailRequest.setSource(from);
    }

    public void sendTemplatedEmail(EmailTemplate template){
        emailRequest.setDestination(new Destination().withToAddresses(template.getDestination()));
        emailRequest.setTemplate(template.getTemplateName());
        JSONObject templateData = new JSONObject();
        template.getTemplateVariables().forEach(templateData::put);
        emailRequest.setTemplateData(templateData.toString());
        client.sendTemplatedEmail(emailRequest);
        log.info("Email has been sent successfully");
    }


}
