package com.example.CoopCredit.domain.model;

import com.example.CoopCredit.domain.model.affiliate.RiskEvaluation;
import com.example.CoopCredit.domain.model.affiliate.RiskLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RiskEvaluationTest {

    @Test
    void createRiskEvaluation_shouldSetFieldsCorrectly() {
        RiskEvaluation evaluation = new RiskEvaluation(
                650,
                RiskLevel.MEDIUM,
                "Stable income, moderate risk"
        );

        assertEquals(650, evaluation.getScore());
        assertEquals(RiskLevel.MEDIUM, evaluation.getRiskLevel());
        assertEquals("Stable income, moderate risk", evaluation.getDecisionReason());
    }

    @Test
    void riskEvaluation_shouldDetectHighRisk() {
        RiskEvaluation eval = new RiskEvaluation(300, RiskLevel.HIGH, "Low credit history");
        assertEquals(RiskLevel.HIGH, eval.getRiskLevel());
    }
}
