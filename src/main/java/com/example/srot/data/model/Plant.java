package com.example.srot.data.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "plants")
public class Plant extends BaseEntity {

    @Column(name = "description")
    private String description;

    @Column(name = "tags")
    private String tags;

    @Column(name = "plant_status")
    private String plantStatus;

    @Column(name = "plant_capacity")
    private Long plantCapacity;

    @Column(name = "plant_cost")
    private Long plantCost;

    @Column(name = "daily_unit_benchmark")
    private Long dailyUnitBenchmark;

    @ManyToOne
    private Listing listing;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "pin_code")
    private String pinCode;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

//    @ManyToOne
//    private Contract contract;

}
