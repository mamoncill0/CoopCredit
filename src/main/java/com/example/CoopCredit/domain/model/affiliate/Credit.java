package com.example.CoopCredit.domain.model.affiliate;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Credit {
    private Long id;
    private Affiliate affiliateDocument;
    private BigDecimal amount;
    private int term;
    private BigDecimal proposedRate;
    private LocalDate requestDate;
    private StatusCredit statusCredit;
    private RiskEvaluation associatedEvaluation;


    public Credit() {
    }

    public Credit(Affiliate affiliate, BigDecimal amount, int term, BigDecimal proposedRate) {
        if (!affiliate.canRequestAppointment()) {
            throw new IllegalStateException("El afiliado debe estar ACTIVO para solicitar un crédito.");
        }

        this.affiliateDocument = affiliate; // CORRECCIÓN: Asigna el parámetro 'affiliate'
        this.amount = amount;
        this.term = term;
        this.proposedRate = proposedRate;
        this.requestDate = LocalDate.now();
        this.statusCredit = StatusCredit.PENDING; // Siempre inicia como PENDIENTE
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Affiliate getAffiliateDocument() {
        return affiliateDocument;
    }

    public void setAffiliateDocument(Affiliate affiliateDocument) {
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

    public RiskEvaluation getAssociatedEvaluation() {
        return associatedEvaluation;
    }

    public void setAssociatedEvaluation(RiskEvaluation associatedEvaluation) {
        this.associatedEvaluation = associatedEvaluation;
    }

    public void validateAmount(int amount){
        if (amount <= 0){
            throw new IllegalArgumentException("El monto del credito debe ser mayor a 0");
        }
    }

    public void approve(){
        if (this.statusCredit == StatusCredit.APPROVED){
            throw new IllegalStateException("El credito ya esta aprobado");
        }
        this.statusCredit = StatusCredit.APPROVED;
    }

    public void reject(){
        if (this.statusCredit == StatusCredit.REJECTED){
            throw new IllegalStateException("El credito ya esta rechazado");
        }
        this.statusCredit = StatusCredit.REJECTED;
    }
}
