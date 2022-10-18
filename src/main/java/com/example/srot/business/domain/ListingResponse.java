package com.example.srot.business.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListingResponse extends DefaultResponse{

    private String docId;
    private String clientEmail;
    private String invoiceId;


}
