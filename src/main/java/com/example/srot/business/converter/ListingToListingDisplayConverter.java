package com.example.srot.business.converter;

import com.example.srot.business.domain.ListingDisplay;
import com.example.srot.data.model.Listing;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ListingToListingDisplayConverter implements Converter<Listing, ListingDisplay> {

    @Synchronized
    @Nullable
    @Override
    public ListingDisplay convert(Listing source) {
        if(source == null) {
            return null;
        }

        final ListingDisplay listingDisplay = new ListingDisplay();
        listingDisplay.setListingId(source.getId());
        listingDisplay.setStartDate(source.getStartDate().toString());
        listingDisplay.setFundRaised(source.getFundRaised());
        listingDisplay.setFundLimit(source.getFundLimit());
        listingDisplay.setEndDate(source.getEndDate().toString());
        listingDisplay.setListingStatus(source.getListingStatus().name());

        return listingDisplay;
    }
}
