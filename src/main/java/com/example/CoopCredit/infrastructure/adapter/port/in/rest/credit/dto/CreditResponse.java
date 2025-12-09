package com.example.CoopCredit.infrastructure.adapter.port.in.rest.credit.dto; // Define el paquete para los DTOs de crédito.

import com.example.CoopCredit.domain.model.affiliate.StatusCredit; // Importa el enum StatusCredit.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.risk.dto.RiskEvaluationResponse; // Importa el DTO de respuesta de evaluación de riesgo.

import java.math.BigDecimal; // Importa BigDecimal.
import java.time.LocalDate; // Importa LocalDate.

public class CreditResponse { // Clase que representa la respuesta de una solicitud de crédito.

    private Long id; // ID de la solicitud de crédito.
    private Long affiliateDocument; // Documento del afiliado.
    private BigDecimal amount; // Monto solicitado.
    private int term; // Plazo en meses.
    private BigDecimal proposedRate; // Tasa propuesta.
    private LocalDate requestDate; // Fecha de solicitud.
    private StatusCredit statusCredit; // Estado de la solicitud de crédito.
    private RiskEvaluationResponse associatedEvaluation; // Evaluación de riesgo asociada.

    // Constructor para inicializar todos los campos.
    public CreditResponse(Long id, Long affiliateDocument, BigDecimal amount, int term, BigDecimal proposedRate, LocalDate requestDate, StatusCredit statusCredit, RiskEvaluationResponse associatedEvaluation) {
        this.id = id;
        this.affiliateDocument = affiliateDocument;
        this.amount = amount;
        this.term = term;
        this.proposedRate = proposedRate;
        this.requestDate = requestDate;
        this.statusCredit = statusCredit;
        this.associatedEvaluation = associatedEvaluation;
    }

    // Getters y Setters para los campos.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAffiliateDocument() {
        return affiliateDocument;
    }

    public void setAffiliateDocument(Long affiliateDocument) {
        this.affiliateDocument = affiliateDocument;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public BigDecimal getProposedRate() {
        return proposedRate;
    }

    public void setProposedRate(BigDecimal proposedRate) {
        this.proposedRate = proposedRate;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public StatusCredit getStatusCredit() {
        return statusCredit;
    }

    public void setStatusCredit(StatusCredit statusCredit) {
        this.statusCredit = statusCredit;
    }

    public RiskEvaluationResponse getAssociatedEvaluation() {
        return associatedEvaluation;
    }

    public void setAssociatedEvaluation(RiskEvaluationResponse associatedEvaluation) {
        this.associatedEvaluation = associatedEvaluation;
    }
}
