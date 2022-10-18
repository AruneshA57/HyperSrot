package com.example.srot.business.domain;

import com.example.srot.data.model.Listing;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListingDisplay {

    private Long listingId;
    private Long fundLimit;
    private String startDate;
    private String endDate;
    private Long fundRaised;
    private String listingStatus;
    private ReturnTable returnTable60;
    private ReturnTable returnTable30;
    private Set<PlantDisplay> plants = new HashSet<>();

    public ListingDisplay(Listing listing) {
        this.listingId = listing.getId();
        this.fundLimit = listing.getFundLimit();
        this.startDate = String.valueOf(listing.getStartDate());
        this.endDate = String.valueOf(listing.getEndDate());
        this.fundRaised = listing.getFundRaised();
        this.listingStatus = String.valueOf(listing.getListingStatus());
        listing.getPlants().forEach(plant -> {
            plants.add(new PlantDisplay(plant));
        });
        this.returnTable60 = null;
        this.returnTable30 = null;
    }

    public ListingDisplay(Listing listing, ReturnTable returnTable60, ReturnTable returnTable30) {
        this(listing);
        this.returnTable60 = returnTable60;
        this.returnTable30 = returnTable30;
    }
}
