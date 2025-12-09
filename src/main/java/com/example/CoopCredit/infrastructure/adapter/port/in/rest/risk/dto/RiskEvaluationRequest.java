package com.example.CoopCredit.infrastructure.adapter.port.in.rest.risk.dto; // Paquete para DTOs en la capa de infraestructura.

import java.math.BigDecimal; // Importa BigDecimal para manejar valores monetarios.

public class RiskEvaluationRequest { // Define el objeto de transferencia de datos para la solicitud.

    private String document; // Declara el campo para el número de documento.
    private BigDecimal amount; // Declara el campo para el monto del crédito.
    private int term; // Declara el campo para el plazo en meses.

    // Getters y Setters para los campos de la clase.

    public String getDocument() { // Método para obtener el documento.
        return document; // Retorna el valor del documento.
    }

    public void setDocument(String document) { // Método para establecer el documento.
        this.document = document; // Asigna el valor al campo de la clase.
    }

    public BigDecimal getAmount() { // Método para obtener el monto.
        return amount; // Retorna el valor del monto.
    }

    public void setAmount(BigDecimal amount) { // Método para establecer el monto.
        this.amount = amount; // Asigna el valor al campo de la clase.
    }

    public int getTerm() { // Método para obtener el plazo.
        return term; // Retorna el valor del plazo.
    }

    public void setTerm(int term) { // Método para establecer el plazo.
        this.term = term; // Asigna el valor al campo de la clase.
    }
}
