package com.example.srot.web.controller;

import com.example.srot.business.domain.DefaultResponse;
import com.example.srot.business.domain.ListingDisplay;
import com.example.srot.business.service.ListingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
public class ListingWebController {

    private final ListingService listingService;

    public ListingWebController(ListingService listingService) {
        this.listingService = listingService;
    }


    @PostMapping("/sign/generateDoc")
    public ResponseEntity<DefaultResponse> generateDocument(@RequestBody Map<String,Long> details){
        Long listingId = details.get("listing_id");
        Long months = details.get("term_in_months");
        Long amount = details.get("amount");
        DefaultResponse responseBody = new DefaultResponse(
                DefaultResponse.Status.SUCCESS,
                listingService.generateDoc(amount, months, listingId),
                "document generated"
        );
        return ResponseEntity.ok(responseBody);
    }

//    @PostMapping("/sign/invest")
//    public ResponseEntity<DefaultResponse> invest(@RequestParam Long documentId,
//                                                  @RequestParam Long invoiceId){
//        return ResponseEntity.ok(listingService.invest(documentId,invoiceId));
//    }
    
    @GetMapping("/assets")
    public ResponseEntity<DefaultResponse> getListings() {
        DefaultResponse responseBody = new DefaultResponse(
                DefaultResponse.Status.SUCCESS,
                Map.of("assets", listingService.getAllListings()),
                "assets"
        );
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/assets/details")
    public ResponseEntity<DefaultResponse> getListing(@RequestBody Map<String, Long> map) {
        DefaultResponse responseBody = new DefaultResponse(
                DefaultResponse.Status.SUCCESS,
                Map.of("listing ", listingService.getListing(map.get("id"))),
                "assets details"
        );
        return ResponseEntity.ok(responseBody);
    }

    /*@GetMapping("assets/estimate")
    public ResponseEntity<ReturnTable> getReturns(@RequestBody ReturnEstimateDto returnEstimateDto) {
        return ResponseEntity.ok(listingService.getReturns(returnEstimateDto));
    }*/
}
