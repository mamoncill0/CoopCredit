package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.adapter; // Define el paquete para los adaptadores de persistencia.

import com.example.CoopCredit.domain.model.affiliate.RiskEvaluation; // Importa el modelo de dominio RiskEvaluation.
import com.example.CoopCredit.domain.port.out.RiskEvaluationRepositoryPort; // Importa el puerto de salida RiskEvaluationRepositoryPort.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.RiskEvaluationEntity; // Importa la entidad RiskEvaluationEntity de persistencia.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository.RiskEvaluationJpaRepository; // Importa el repositorio JPA para RiskEvaluationEntity.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.stereotype.Component; // Importa @Component para que Spring detecte esta clase como un bean.

@Component // Marca esta clase como un componente de Spring.
public class RiskEvaluationPersistenceAdapter implements RiskEvaluationRepositoryPort { // Implementa el puerto de salida RiskEvaluationRepositoryPort.

    private final RiskEvaluationJpaRepository riskEvaluationJpaRepository; // Inyecta el repositorio JPA para RiskEvaluationEntity.

    @Autowired // Constructor para inyección de dependencias.
    public RiskEvaluationPersistenceAdapter(RiskEvaluationJpaRepository riskEvaluationJpaRepository) {
        this.riskEvaluationJpaRepository = riskEvaluationJpaRepository; // Asigna el repositorio JPA de evaluaciones de riesgo.
    }

    // Convierte un objeto RiskEvaluationEntity (persistencia) a un objeto RiskEvaluation (dominio).
    public RiskEvaluation toDomainRiskEvaluation(RiskEvaluationEntity entity) { // Visibilidad cambiada a public.
        // CORRECCIÓN: Crear RiskEvaluation del dominio con el ID de la entidad.
        return new RiskEvaluation(entity.getId(), entity.getScore(), entity.getRiskLevel(), entity.getDecisionReason()); // Crea y retorna un RiskEvaluation del dominio.
    }

    // Convierte un objeto RiskEvaluation (dominio) a un objeto RiskEvaluationEntity (persistencia).
    public RiskEvaluationEntity toPersistenceRiskEvaluationEntity(RiskEvaluation domain) { // Visibilidad cambiada a public.
        // CORRECCIÓN: Crear RiskEvaluationEntity de persistencia.
        RiskEvaluationEntity entity = new RiskEvaluationEntity(domain.getScore(), domain.getRiskLevel(), domain.getDecisionReason());
        entity.setId(domain.getId()); // Asigna el ID del dominio a la entidad.
        return entity; // Crea y retorna un RiskEvaluationEntity de persistencia.
    }

    @Override // Implementación del método save del puerto.
    public RiskEvaluation save(RiskEvaluation riskEvaluation) {
        RiskEvaluationEntity entity = toPersistenceRiskEvaluationEntity(riskEvaluation); // Convierte el RiskEvaluation de dominio a RiskEvaluationEntity de persistencia.
        RiskEvaluationEntity savedEntity = riskEvaluationJpaRepository.save(entity); // Guarda la entidad en la base de datos.
        return toDomainRiskEvaluation(savedEntity); // Convierte la entidad guardada de nuevo a RiskEvaluation de dominio y lo retorna.
    }
}
