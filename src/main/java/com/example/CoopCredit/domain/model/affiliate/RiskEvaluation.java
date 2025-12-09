package com.example.CoopCredit.domain.model.affiliate;

public class RiskEvaluation {

    private Integer score;
    private RiskLevel riskLevel;
    private String decisionReason;

    public RiskEvaluation() {
    }

    public RiskEvaluation(Integer score, RiskLevel riskLevel, String decisionReason) {
        this.score = score;
        this.riskLevel = riskLevel;
        this.decisionReason = decisionReason;
    }

    // Getters
    public Integer getScore() {
        return score;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public String getDecisionReason() {
        return decisionReason;
    }
}
