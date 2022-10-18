package com.example.srot.data.model;

import com.example.srot.business.domain.QuestionnaireDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class InvestorQuestionnaire extends BaseEntity{

    @OneToOne
    private Investor investor;
    @Column(name = "MONTHLY_INCOME")
    private String monthlyIncome;
    @Column(name = "MONTHLY_SAVINGS")
    private String monthlySavings;
    @Column(name = "SAVINGS_PREFERENCE")
    private String savingsPreference;
    @Column(name = "INVESTMENT_INTENTION")
    private String investmentIntention;
    @Column(name = "INVESTMENT_PLAN")
    private String investmentPlan;
    @Column(name = "PAYMENT_FREQUENCY")
    private String paymentFrequency;
    @Column(name = "FEATURES_SUGGESTION", columnDefinition = "TEXT")
    private String featuresSuggestion;
    @Column(name = "QUESTIONS", columnDefinition = "TEXT")
    private String questions;
    @Column(name = "WEBINAR")
    private boolean webinar;

    public InvestorQuestionnaire() {}

    public InvestorQuestionnaire(Investor investor, QuestionnaireDTO dto) {
        this.setId(dto.getId());
        this.investor = investor;
        this.monthlyIncome = dto.getMonthlyIncome();
        this.monthlySavings = dto.getMonthlySavings();
        this.savingsPreference = dto.getSavingsPreference();
        this.investmentIntention = dto.getInvestmentIntention();
        this.investmentPlan = dto.getInvestmentPlan();
        this.paymentFrequency = dto.getPaymentFrequency();
        this.featuresSuggestion = dto.getFeaturesSuggestion();
        this.questions = dto.getQuestions();
        this.webinar = dto.isWebinar();
    }

}
