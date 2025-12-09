package com.example.CoopCredit.infrastructure.adapter.port.in.rest.credit.dto; // Define el paquete para los DTOs de crédito.

import jakarta.validation.constraints.DecimalMin; // Importa la anotación para validar un valor decimal mínimo.
import jakarta.validation.constraints.Min; // Importa la anotación para validar un valor mínimo.
import jakarta.validation.constraints.NotNull; // Importa la anotación para validar que el campo no sea nulo.

import java.math.BigDecimal; // Importa BigDecimal para manejar valores monetarios.

public class CreditRequest { // Clase que representa la solicitud de creación de un crédito.

    @NotNull(message = "Affiliate document cannot be null") // Valida que el documento del afiliado no sea nulo.
    private Long affiliateDocument; // Documento del afiliado solicitante.

    @NotNull(message = "Amount cannot be null") // Valida que el monto no sea nulo.
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0") // Valida que el monto sea mayor que 0.
    private BigDecimal amount; // Monto solicitado.

    @Min(value = 1, message = "Term must be at least 1 month") // Valida que el plazo sea al menos 1 mes.
    private int term; // Plazo en meses.

    @NotNull(message = "Proposed rate cannot be null") // Valida que la tasa propuesta no sea nula.
    @DecimalMin(value = "0.0", message = "Proposed rate cannot be negative") // Valida que la tasa propuesta no sea negativa.
    private BigDecimal proposedRate; // Tasa propuesta.

    // Getters y Setters para los campos.

    public Long getAffiliateDocument() { // Método para obtener el documento del afiliado.
        return affiliateDocument; // Retorna el documento del afiliado.
    }

    public void setAffiliateDocument(Long affiliateDocument) { // Método para establecer el documento del afiliado.
        this.affiliateDocument = affiliateDocument; // Asigna el documento del afiliado.
    }

    public BigDecimal getAmount() { // Método para obtener el monto.
        return amount; // Retorna el monto.
    }

    public void setAmount(BigDecimal amount) { // Método para establecer el monto.
        this.amount = amount; // Asigna el monto.
    }

    public int getTerm() { // Método para obtener el plazo.
        return term; // Retorna el plazo.
    }

    public void setTerm(int term) { // Método para establecer el plazo.
        this.term = term; // Asigna el plazo.
    }

    public BigDecimal getProposedRate() { // Método para obtener la tasa propuesta.
        return proposedRate; // Retorna la tasa propuesta.
    }

    public void setProposedRate(BigDecimal proposedRate) { // Método para establecer la tasa propuesta.
        this.proposedRate = proposedRate; // Asigna la tasa propuesta.
    }
}
