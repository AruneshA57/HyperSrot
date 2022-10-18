package com.example.srot.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "investor_rate")
public class InvestorRate extends BaseEntity {
    private Long investorRate;
    private Integer tenure;
    private boolean active;
}
