package com.example.CoopCredit.domain.port.out; // Define el paquete para los puertos de salida del dominio.

import com.example.CoopCredit.domain.model.affiliate.RiskEvaluation; // Importa el modelo de dominio RiskEvaluation.

public interface RiskEvaluationRepositoryPort { // Define la interfaz del puerto para la gestión de evaluaciones de riesgo.

    RiskEvaluation save(RiskEvaluation riskEvaluation); // Método para guardar una evaluación de riesgo.
}
