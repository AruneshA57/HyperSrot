package com.example.srot.data.exceptions;

public class ListingException extends RuntimeException {
    public ListingException(Long id) {
        super("Listing: " + id + " not found");
    }

    public ListingException(Long id, Long amount) {
        super("Cannot invest more than Rs. " + amount + " in listing: " + id);
    }
}
