package com.example.srot.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "INVOICE")
@Setter
@Getter
public class Invoice extends BaseEntity{

    private String invoiceBill;
    @ManyToOne
    private Investor investor;
    @ManyToOne
    private Listing listing;
    private Long amount;
    private Integer tenure;

}
