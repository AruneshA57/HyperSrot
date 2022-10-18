package com.example.srot.data.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "debit_investment_transaction")
public class DebitInvestmentTransaction extends Transaction{
    @OneToOne
    private Investment investment;
}
