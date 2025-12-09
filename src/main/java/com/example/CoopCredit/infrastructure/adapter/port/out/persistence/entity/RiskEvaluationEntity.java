package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity; // Define el paquete para las entidades de persistencia.

import com.example.CoopCredit.domain.model.affiliate.RiskLevel; // Importa el enum RiskLevel del dominio.
import jakarta.persistence.*; // Importa todas las anotaciones de JPA.

@Entity // Marca esta clase como una entidad JPA.
@Table(name = "risk_evaluations") // Especifica el nombre de la tabla en la base de datos.
public class RiskEvaluationEntity { // Define la entidad de evaluación de riesgo para la capa de persistencia.

    @Id // Marca este campo como la clave primaria.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la estrategia de generación de ID.
    private Long id; // Identificador único de la evaluación de riesgo.

    @Column(nullable = false) // Mapea a una columna no nula.
    private Integer score; // Puntaje de riesgo.

    @Enumerated(EnumType.STRING) // Especifica que el enum 'RiskLevel' debe persistirse como una cadena de texto.
    @Column(name = "risk_level", nullable = false, length = 20) // Mapea a una columna no nula con nombre y longitud específicos.
    private RiskLevel riskLevel; // Nivel de riesgo.

    @Column(name = "decision_reason", length = 255) // Mapea a una columna con longitud máxima.
    private String decisionReason; // Razón de la decisión.

    // Constructor por defecto requerido por JPA.
    public RiskEvaluationEntity() {
    }

    // Constructor para crear una instancia de RiskEvaluationEntity con todos los datos.
    public RiskEvaluationEntity(Integer score, RiskLevel riskLevel, String decisionReason) {
        this.score = score; // Inicializa el score.
        this.riskLevel = riskLevel; // Inicializa el nivel de riesgo.
        this.decisionReason = decisionReason; // Inicializa la razón de la decisión.
    }

    // Getters y Setters para los campos de la entidad.

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
