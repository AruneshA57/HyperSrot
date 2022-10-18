package com.example.srot.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "investor_returns")
public class InvestorReturns extends BaseEntity {
    @Column(name = "cost")
    private Long costPerKW;
    private Long tds;
    @Column(name = "units")
    private Long unitsPerDayPerKW;
    private Long powerDepreciation;
    @Column(name = "power_depreciation_interval")
    private Long powerDepreciationIntervalInMonths;
    @Column(name = "value_depreciation")
    private Long valueDepreciationPerYear;
    private boolean active;
}
