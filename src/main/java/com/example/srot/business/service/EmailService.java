package com.example.srot.business.service;

import com.example.srot.business.domain.DefaultResponse;
import com.example.srot.business.domain.EmailAwsClient;
import com.example.srot.business.domain.EmailTemplate;
import com.example.srot.data.model.EmailCode;
import com.example.srot.data.model.Profile;
import com.example.srot.data.repository.EmailCodeRepository;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

import static com.example.srot.data.model.EmailCode.Status.PENDING;

@Service
@Slf4j
public class EmailService {

    @Value("${srot.domain.url}")
    private String domain;
    private final EmailAwsClient emailClient;
    private final EmailCodeRepository emailRepository;

    @Autowired
    public EmailService(EmailAwsClient emailClient, EmailCodeRepository emailRepository) {
        this.emailClient = emailClient;
        this.emailRepository = emailRepository;

    }

    @Transactional
    public void sendConfirmationEmail(Profile user){
        String code = RandomString.make(64);
        String confirmationLink = domain + "signup/verify_email?code=" + code;
        EmailCode confirmationCode = new EmailCode(user.getId(), PENDING,code);
        emailRepository.save(confirmationCode);
        EmailTemplate template = new EmailTemplate();
        template.setDestination(user.getEmail());
        template.setTemplateName("ConfirmationEmailTemplate");
        template.setTemplateVariables(Map.of("confirmationLink",confirmationLink));
        log.info("Sending Email");
        emailClient.sendTemplatedEmail(template);
    }

    public void sendTemplatedEmail(EmailTemplate template) {
        log.info("Sending Email");
        emailClient.sendTemplatedEmail(template);
    }

}