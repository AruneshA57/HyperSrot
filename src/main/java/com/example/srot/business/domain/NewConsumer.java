package com.example.srot.business.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewConsumer extends NewUser{

   private String pointOfContact;
   private String referralCode;

}
