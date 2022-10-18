package com.example.srot.data.model;

import com.example.srot.business.domain.NewConsumer;
import com.example.srot.business.domain.NewUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "consumers")
@Getter
@Setter
public class Consumer extends Profile{

    private String pointOfContact;
    private String referralCode;

    public Consumer() {}

    public Consumer(NewConsumer newUser) {

        setName(newUser.getName());
        setPhoneNumber(newUser.getPhone());
        setEmail(newUser.getEmail());
        setJoiningDate(Date.valueOf(LocalDate.now()));
        LocalDate temp = newUser.getDateOfBirth()==null?null:
                LocalDate.parse(newUser.getDateOfBirth(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        setDateOfBirth(temp==null?null:Date.valueOf(temp));
        setPointOfContact(newUser.getPointOfContact());
        setReferralCode(newUser.getReferralCode());

    }
}
