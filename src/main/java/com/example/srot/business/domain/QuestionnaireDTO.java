package com.example.srot.business.domain;

import com.example.srot.data.model.InvestorQuestionnaire;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionnaireDTO{

    private Long id;
    private Long investorId;
    private String monthlyIncome;
    private String monthlySavings;
    private String savingsPreference;
    private String investmentIntention;
    private String investmentPlan;
    private String paymentFrequency;
    private String featuresSuggestion;
    private String questions;
    private boolean webinar;

    public QuestionnaireDTO() {
    }

    public QuestionnaireDTO(InvestorQuestionnaire entity) {
        this.id = entity.getId();
        this.investorId = entity.getInvestor().getId();
        this.monthlyIncome = entity.getMonthlyIncome();
        this.monthlySavings = entity.getMonthlySavings();
        this.savingsPreference = entity.getSavingsPreference();
        this.investmentIntention = entity.getInvestmentIntention();
        this.investmentPlan = entity.getInvestmentPlan();
        this.paymentFrequency = entity.getPaymentFrequency();
        this.featuresSuggestion = entity.getFeaturesSuggestion();
        this.questions = entity.getQuestions();
        this.webinar = entity.isWebinar();
    }
}
