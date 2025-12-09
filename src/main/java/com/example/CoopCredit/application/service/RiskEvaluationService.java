package com.example.CoopCredit.application.service; // Define el paquete para los servicios de la aplicación.

import com.example.CoopCredit.infrastructure.adapter.port.in.rest.risk.dto.RiskEvaluationRequest; // Importa el DTO de la solicitud desde la capa de infraestructura.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.risk.dto.RiskEvaluationResponse; // Importa el DTO de la respuesta desde la capa de infraestructura.
import org.springframework.stereotype.Service; // Importa la anotación @Service para registrarlo como un bean de Spring.

@Service // Marca esta clase como un servicio de Spring.
public class RiskEvaluationService { // Define la clase que contiene la lógica de negocio para la evaluación de riesgo.

    public RiskEvaluationResponse evaluate(RiskEvaluationRequest request) { // Método público que orquesta la evaluación.
        // 1. Generate a numeric 'seed' from the document to ensure consistent results.
        long seed = Math.abs((long) request.getDocument().hashCode() % 1000); // Calculates the hash of the document, converts to long, takes absolute value, and applies modulo 1000.

        // 2. Generate a deterministic score based on the seed.
        // The range is from 300 to 950, so there are 651 possible values (950 - 300 + 1).
        int score = 300 + (int) (seed % 651); // Uses the seed to generate a number within the desired range.

        // 3. Classify the risk and define the detail based on the score.
        String riskLevel; // Declares a variable to store the risk level.
        String detail; // Declares a variable to store the detail message.

        if (score <= 500) { // Checks if the score corresponds to HIGH risk.
            riskLevel = "HIGH"; // Assigns the risk level.
            detail = "High-risk credit history."; // Assigns the corresponding detail.
        } else if (score <= 700) { // Checks if the score corresponds to MEDIUM risk.
            riskLevel = "MEDIUM"; // Assigns the risk level.
            detail = "Moderate credit history."; // Assigns the corresponding detail.
        } else { // If not HIGH or MEDIUM, it is assumed to be LOW.
            riskLevel = "LOW"; // Assigns the risk level.
            detail = "Low-risk credit history."; // Assigns the corresponding detail.
        }

        // 4. Build and return the response object.
        return new RiskEvaluationResponse(request.getDocument(), score, riskLevel, detail); // Creates a new response instance with all the calculated data.
    }
}
