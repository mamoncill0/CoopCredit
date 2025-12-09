package com.example.CoopCredit.infrastructure.adapter.port.in.rest.risk.dto; // Paquete para DTOs en la capa de infraestructura.

public class RiskEvaluationResponse { // Define el objeto de transferencia de datos para la respuesta.

    private String document; // Declara el campo para el número de documento.
    private int score; // Declara el campo para el puntaje de riesgo.
    private String riskLevel; // Declara el campo para el nivel de riesgo.
    private String detail; // Declara el campo para el mensaje descriptivo.

    // Constructor para inicializar todos los campos.
    public RiskEvaluationResponse(String document, int score, String riskLevel, String detail) {
        this.document = document; // Inicializa el campo del documento.
        this.score = score; // Inicializa el campo del score.
        this.riskLevel = riskLevel; // Inicializa el campo del nivel de riesgo.
        this.detail = detail; // Inicializa el campo del detalle.
    }

    // Getters y Setters para los campos de la clase.

    public String getDocument() { // Método para obtener el documento.
        return document; // Retorna el valor del documento.
    }

    public void setDocument(String document) { // Método para establecer el documento.
        this.document = document; // Asigna el valor al campo de la clase.
    }

    public int getScore() { // Método para obtener el score.
        return score; // Retorna el valor del score.
    }

    public void setScore(int score) { // Método para establecer el score.
        this.score = score; // Asigna el valor al campo de la clase.
    }

    public String getRiskLevel() { // Método para obtener el nivel de riesgo.
        return riskLevel; // Retorna el valor del nivel de riesgo.
    }

    public void setRiskLevel(String riskLevel) { // Método para establecer el nivel de riesgo.
        this.riskLevel = riskLevel; // Asigna el valor al campo de la clase.
    }

    public String getDetail() { // Método para obtener el detalle.
        return detail; // Retorna el valor del detalle.
    }

    public void setDetail(String detail) { // Método para establecer el detalle.
        this.detail = detail; // Asigna el valor al campo de la clase.
    }
}
