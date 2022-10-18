package com.example.srot.data.repository;

import com.example.srot.data.model.Listing;
import org.springframework.data.repository.CrudRepository;

public interface ListingRepository extends CrudRepository<Listing, Long> {
}
