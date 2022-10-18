package com.example.srot.web.controller;

import com.example.srot.business.domain.InvestorPortfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/investor/portfolio")
public class PortfolioWebServiceController {

    /*private final PortfolioService portfolioService;

    @Autowired
    public PortfolioWebServiceController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public InvestorPortfolio getInvestorPortfolio(@RequestParam(name = "transId", required = true)Long transId) {
        return this.portfolioService.getInvestorPortfolio(transId);
    }*/
}
