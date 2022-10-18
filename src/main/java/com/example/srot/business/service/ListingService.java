package com.example.srot.business.service;

import com.example.srot.business.domain.*;
import com.example.srot.business.service.exceptions.NotFoundException;
import com.example.srot.data.exceptions.ListingException;
import com.example.srot.data.model.*;
import com.example.srot.data.repository.InvoiceRepository;
import com.example.srot.data.repository.ListingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class ListingService {

    @Value("${srot.contract.sign.url}")
    private String signUrl;
    @Value("${srot.contract.sign.auth}")
    private String auth;
    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;
    private final AuthenticationService authenticationService;
    private final ListingRepository listingRepository;
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public ListingService(AuthenticationService authenticationService,
                          ListingRepository listingRepository, InvoiceRepository invoiceRepository) {
        this.authenticationService = authenticationService;
        this.invoiceRepository = invoiceRepository;
        this.mapper = new ObjectMapper();
        this.listingRepository = listingRepository;
        this.restTemplate = new RestTemplateBuilder().build();
    }

    public Set<ListingDisplay> getAllListings() {
        Iterable<Listing> listings = listingRepository.findAll();
        Set<ListingDisplay> listingDisplays = new HashSet<>();
        listings.forEach(listing -> {listingDisplays.add(
                new ListingDisplay(listing, getReturns(listing,60.0), getReturns(listing,30.0)));
        });

        return listingDisplays;
    }

    public ListingDisplay getListing(Long id) {
        Listing listing = listingRepository.findById(id).orElseThrow(() -> new ListingException(id));
        return new ListingDisplay(listing, getReturns(listing, 60.0), getReturns(listing, 30.0));
    }

    private ReturnTable getReturns(Listing listing, Double tenure) {
        InvestorRate investorRate = listing.getInvestorRateByTenure(tenure.longValue());
        InvestorReturns investorReturns = listing.getInvestorReturns();

        double amount = 10000000.0;
        Double numOfKW = amount / investorReturns.getCostPerKW();
        double amountPerMonth = investorRate.getInvestorRate() * numOfKW * investorReturns.getUnitsPerDayPerKW() * 30;
        long totalPayoutPreTax = 0L, totalPayoutPostTax = 0L;

        ReturnRow row = ReturnRow.builder().grossPayout((long) Math.ceil(amountPerMonth)).
                tds((long) Math.ceil(amountPerMonth * investorReturns.getTds() / 100.0)).build();
        row.setNetPayout(row.getGrossPayout() - row.getTds());
        totalPayoutPostTax += row.getNetPayout();
        totalPayoutPreTax += row.getGrossPayout();

        ReturnTable returnTable = new ReturnTable();
        returnTable.addReturnRow(1, row);

        for(int i = 1; i < tenure.intValue(); ++i) {
            if(i % 12 == 0) {
                //plant capacity depreciates every year
                //depreciate returns accordingly
                double depreciation = amountPerMonth * investorReturns.getPowerDepreciation() / 100.0;
                amountPerMonth -= depreciation;
            }

            row = ReturnRow.builder().grossPayout((long) Math.ceil(amountPerMonth)).
                    tds((long) Math.ceil(amountPerMonth * investorReturns.getTds() / 100.0)).build();
            row.setNetPayout(row.getGrossPayout() - row.getTds());
            totalPayoutPostTax += row.getNetPayout();
            totalPayoutPreTax += row.getGrossPayout();
            returnTable.addReturnRow(i + 1, row);
        }

        //current value of plant depreciates every year
        // final payout for the invested amount depreciates accordingly
        double lastPayout = amount * Math.pow(((100 - investorReturns.getValueDepreciationPerYear()) / 100.0),
                tenure / 12.0);
        returnTable.setLastPayout((long) Math.ceil(lastPayout));

        totalPayoutPostTax += lastPayout;
        totalPayoutPreTax += lastPayout;
        returnTable.setTotalPayoutPreTax(totalPayoutPreTax);
        returnTable.setTotalPayoutPostTax(totalPayoutPostTax);
        double retPer = (totalPayoutPreTax - amount) * 100 * 12 / (amount * tenure);
        returnTable.setReturnPercentagePreTax(String.format("%.2f", retPer));

        retPer = (totalPayoutPostTax - amount) * 100 * 12 / (amount * tenure);
        returnTable.setReturnPercentagePostTax(String.format("%.2f", retPer));
        returnTable.setTotalSolarAssets(Math.ceil(numOfKW * 1000));

        return returnTable;
    }

    public Listing validateAndGetListing(Long id, Long amount) {
        Listing listing = listingRepository.findById(id).orElseThrow(() -> new ListingException(id));

        if(listing.getFundRaised() + amount > listing.getFundLimit()) {
            log.error("Fund limit exceeded");
            throw new ListingException(id, listing.getFundLimit() - listing.getFundRaised());
        }

        return listing;
    }

    public Map<String,String> generateDoc(Long amount, Long months, Long listingId) {

        Investor investor = authenticationService.findAuthenticatedInvestor();
        String contractJson = getInvestorContractJson();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setCacheControl(CacheControl.noCache());
        headers.add("Authorization",auth);

        LocalDate now = LocalDate.now();
        String day = String.format("%02d",now.getDayOfMonth());
        String month = String.format("%02d",now.getMonthValue());
        String year = now.getYear()+"";
        String fileName = "con" + year.substring(2)+month+investor.getId()+listingId;

        double gst = (amount/100.0-(amount/100.0/1.05));

        Map<String,String> listingDetails = getListingDetailsFromApi(listingId,months);

        Invoice invoice = new Invoice();
        invoice.setListing(listingRepository.findById(listingId).orElseThrow(()->
                new NotFoundException("No Listing with provided id was found")
        ));
        invoice.setInvestor(investor);
        invoice.setInvoiceBill("BL_"+year.substring(2)+month+day+investor.getId());
        invoice.setAmount(amount);
        invoice = invoiceRepository.save(invoice);

        contractJson = contractJson.replaceAll("<<investor_identifier>>",investor.getEmail());
        contractJson = contractJson.replaceAll("<<file_name>>",fileName);
        contractJson = contractJson.replaceAll("<<day>>",day);
        contractJson = contractJson.replaceAll("<<month>>",month);
        contractJson = contractJson.replaceAll("<<year>>",year);
        contractJson = contractJson.replaceAll("<<investor_aadhar>>","21342145213");//TODO this should be replaced with Investor.getAadharNumber() when we save it in the workflow
        contractJson = contractJson.replaceAll("<<investor_pan>>","1213342423");//TODO this should be replaced with Investor.getPanNumber() when we save it in the workflow
        contractJson = contractJson.replaceAll("<<investor_email>>",investor.getEmail());
        contractJson = contractJson.replaceAll("<<investor_mobile>>",investor.getPhoneNumber());
        contractJson = contractJson.replaceAll("<<investor_name>>",investor.getName());
        contractJson = contractJson.replaceAll("<<contract_id>>",String.valueOf(listingId));
        contractJson = contractJson.replaceAll("<<invoice_bill>>",invoice.getInvoiceBill());
        contractJson = contractJson.replaceAll("<<gst_amount>>",String.valueOf(gst));
        contractJson = contractJson.replaceAll("<<cost_amount>>",String.valueOf(amount));
        contractJson = contractJson.replaceAll("<<term_months>>",String.valueOf(months));
        contractJson = contractJson.replaceAll("<<listing_capacity>>",listingDetails.get("capacity").replace(".",","));
        contractJson = contractJson.replaceAll("<<listing_roi>>",listingDetails.get("roi").replace(".",","));
        contractJson = contractJson.replaceAll("<<listing_rent>>",listingDetails.get("rent").replace(".",","));

        HttpEntity<String> request = new HttpEntity<>(contractJson, headers);
        String digioResponse = restTemplate.postForObject(signUrl,request,String.class);
        Map<String, Object> digioJson = null;
        try {
             digioJson = mapper.readValue(digioResponse,Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Map<String,String> response = new HashMap<>();
        response.put("document_id",(String)digioJson.get("id"));
        response.put("investor_email",investor.getEmail());
        response.put("invoice_id",String.valueOf(invoice.getId()));
        return response;

    }

    private Map<String, String> getListingDetailsFromApi(Long listingId,Long months) {

        ListingDisplay listingDisplay = getListing(listingId);
        ReturnTable table = months==60?listingDisplay.getReturnTable60():listingDisplay.getReturnTable30();
        String rent = String.valueOf(table.getRows().get(1).getGrossPayout()/100.0);
        String capacity = String.valueOf(table.getTotalSolarAssets());
        String roi = String.valueOf(table.getReturnPercentagePreTax());
        Map<String, String> listingDetails = new HashMap<>();
        listingDetails.put("rent",rent);
        listingDetails.put("capacity",capacity);
        listingDetails.put("roi",roi);
        return listingDetails;
    }

    private String getInvestorContractJson() {

        InputStream is = getClass().getClassLoader().getResourceAsStream("digio/investor_contract.json");
        if(is==null){
            throw new NotFoundException("Investor_Contract json couldn't be found");
        }
        StringBuilder builder = new StringBuilder();
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public Listing save(Listing listing) {
        return listingRepository.save(listing);
    }
}
