package com.example.CoopCredit.domain.model.affiliate; // Define el paquete para los modelos de afiliado del dominio.

public class RiskEvaluation { // Clase de modelo de dominio para una evaluación de riesgo.

    private Long id; // Identificador único de la evaluación de riesgo (añadido para consistencia).
    private Integer score; // Puntaje de riesgo.
    private RiskLevel riskLevel; // Nivel de riesgo.
    private String decisionReason; // Razón de la decisión.

    // Constructor por defecto.
    public RiskEvaluation() {
    }

    // Constructor para crear una instancia de RiskEvaluation con todos los datos.
    public RiskEvaluation(Long id, Integer score, RiskLevel riskLevel, String decisionReason) {
        this.id = id; // Inicializa el ID.
        this.score = score; // Inicializa el score.
        this.riskLevel = riskLevel; // Inicializa el nivel de riesgo.
        this.decisionReason = decisionReason; // Inicializa la razón de la decisión.
    }

    // Constructor sin ID (para cuando se crea por primera vez).
    public RiskEvaluation(Integer score, RiskLevel riskLevel, String decisionReason) {
        this(null, score, riskLevel, decisionReason); // Llama al constructor completo con ID nulo.
    }

    // Getters y Setters para los campos.

    public Long getId() { // Método para obtener el ID.
        return id; // Retorna el ID.
    }

    public void setId(Long id) { // Método para establecer el ID.
        this.id = id; // Asigna el ID.
    }

    public Integer getScore() { // Método para obtener el score.
        return score; // Retorna el score.
    }

    public void setScore(Integer score) { // Método para establecer el score.
        this.score = score; // Asigna el score.
    }

    public RiskLevel getRiskLevel() { // Método para obtener el nivel de riesgo.
        return riskLevel; // Retorna el nivel de riesgo.
    }

    public void setRiskLevel(RiskLevel riskLevel) { // Método para establecer el nivel de riesgo.
        this.riskLevel = riskLevel; // Asigna el nivel de riesgo.
    }

    public String getDecisionReason() { // Método para obtener la razón de la decisión.
        return decisionReason; // Retorna la razón de la decisión.
    }

    public void setDecisionReason(String decisionReason) { // Método para establecer la razón de la decisión.
        this.decisionReason = decisionReason; // Asigna la razón de la decisión.
    }
}
