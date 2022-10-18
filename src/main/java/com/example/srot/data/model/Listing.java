package com.example.srot.data.model;

import com.example.srot.data.enums.ListingStatus;
import com.razorpay.Plan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "listings")
public class Listing extends BaseEntity {

    @Column(name = "fund_limit")
    private Long fundLimit;

    @Column(name = "start_date")
    private Date startDate;

    @OneToMany(mappedBy = "listing")
    private Set<Invoice> invoice;

    /*@Column(name = "duration")
    private Integer duration;*/

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "fund_raised")
    private Long fundRaised;

    @ManyToOne(cascade = {CascadeType.ALL})
    private InvestorRate investorRate30;

    @ManyToOne(cascade = {CascadeType.ALL})
    private InvestorRate investorRate60;

    @ManyToOne(cascade = {CascadeType.ALL})
    private InvestorReturns investorReturns;

    @Column(name = "listing_status")
    @Enumerated(value = EnumType.STRING)
    private ListingStatus listingStatus;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "listing", cascade = CascadeType.ALL)
    private Set<Plant> plants = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "listing", cascade = CascadeType.ALL)
    private Set<Investment> investments = new HashSet<>();

    public Listing() {
        fundLimit = 0L;
    }

    public void addInvestment(Investment investment) {
        if(fundRaised + investment.getAmount() > fundLimit) {
            throw new RuntimeException("Investment amount exceeds fund limits. Investment amount: "
                    + investment.getAmount() + " Fund required: " + (fundLimit - fundRaised));
        }

        fundRaised += investment.getAmount();
        investments.add(investment);
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
        fundLimit += plant.getPlantCapacity() * investorReturns.getCostPerKW() / 1000;
    }

    public InvestorRate getInvestorRateByTenure(Long tenure) {
        return (tenure == 30L) ? investorRate30 : investorRate60;
    }

}
