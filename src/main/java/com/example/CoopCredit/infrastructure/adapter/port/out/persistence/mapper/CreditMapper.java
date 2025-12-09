package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.mapper; // Define el paquete para los mappers de persistencia.

import com.example.CoopCredit.domain.model.affiliate.Credit; // Importa el modelo de dominio Credit.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.adapter.AffiliatePersistenceAdapter; // Importa el adaptador de persistencia de afiliados.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.adapter.RiskEvaluationPersistenceAdapter; // Importa el adaptador de persistencia de evaluaciones de riesgo.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.CreditEntity; // Importa la entidad CreditEntity.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.stereotype.Component; // Importa @Component para que Spring detecte esta clase como un bean.

@Component // Marca esta clase como un componente de Spring.
public class CreditMapper { // Clase para mapear entre el modelo de dominio Credit y la entidad de persistencia CreditEntity.

    private final AffiliatePersistenceAdapter affiliatePersistenceAdapter; // Inyecta el adaptador de persistencia de afiliados.
    private final RiskEvaluationPersistenceAdapter riskEvaluationPersistenceAdapter; // Inyecta el adaptador de persistencia de evaluaciones de riesgo.

    @Autowired // Constructor para inyección de dependencias.
    public CreditMapper(AffiliatePersistenceAdapter affiliatePersistenceAdapter, RiskEvaluationPersistenceAdapter riskEvaluationPersistenceAdapter) {
        this.affiliatePersistenceAdapter = affiliatePersistenceAdapter; // Asigna el adaptador de afiliados.
        this.riskEvaluationPersistenceAdapter = riskEvaluationPersistenceAdapter; // Asigna el adaptador de evaluaciones de riesgo.
    }

    // ==========================================
    // Domain → Entity
    // ==========================================
    public CreditEntity toEntity(Credit credit) {
        if (credit == null) {
            return null;
        }

        CreditEntity entity = new CreditEntity();
        entity.setId(credit.getId());
        // Mapea el Affiliate del dominio a AffiliateEntity usando el adaptador.
        entity.setAffiliate(affiliatePersistenceAdapter.toPersistenceAffiliateEntity(credit.getAffiliateDocument()));
        entity.setAmount(credit.getAmount());
        entity.setTerm(credit.getTerm());
        entity.setProposedRate(credit.getProposedRate());
        entity.setRequestDate(credit.getRequestDate());
        entity.setStatusCredit(credit.getStatusCredit());

        if (credit.getAssociatedEvaluation() != null) {
            // Mapea RiskEvaluation del dominio a RiskEvaluationEntity usando el adaptador.
            entity.setAssociatedEvaluation(riskEvaluationPersistenceAdapter.toPersistenceRiskEvaluationEntity(credit.getAssociatedEvaluation()));
        }

        return entity;
    }

    // ==========================================
    // Entity → Domain
    // ==========================================
    public Credit toDomain(CreditEntity entity) {
        if (entity == null) {
            return null;
        }

        // Crea el objeto Credit usando el constructor principal.
        Credit credit = new Credit(
                affiliatePersistenceAdapter.toDomainAffiliate(entity.getAffiliate()),
                entity.getAmount(),
                entity.getTerm(),
                entity.getProposedRate()
        );

        // Asigna las propiedades restantes usando los setters.
        credit.setId(entity.getId());
        credit.setRequestDate(entity.getRequestDate());
        credit.setStatusCredit(entity.getStatusCredit());

        if (entity.getAssociatedEvaluation() != null) {
            credit.setAssociatedEvaluation(riskEvaluationPersistenceAdapter.toDomainRiskEvaluation(entity.getAssociatedEvaluation()));
        }

        return credit;
    }

    // Método para actualizar un Credit de dominio con datos de una CreditEntity.
    // Útil para operaciones de actualización donde no se crea un nuevo objeto Credit.
    public void updateDomainFromEntity(Credit credit, CreditEntity entity) {
        if (credit == null || entity == null) {
            return;
        }
        credit.setId(entity.getId());
        credit.setAffiliateDocument(affiliatePersistenceAdapter.toDomainAffiliate(entity.getAffiliate()));
        credit.setAmount(entity.getAmount());
        credit.setTerm(entity.getTerm());
        credit.setProposedRate(entity.getProposedRate());
        credit.setRequestDate(entity.getRequestDate());
        credit.setStatusCredit(entity.getStatusCredit());
        if (entity.getAssociatedEvaluation() != null) {
            credit.setAssociatedEvaluation(riskEvaluationPersistenceAdapter.toDomainRiskEvaluation(entity.getAssociatedEvaluation()));
        } else {
            credit.setAssociatedEvaluation(null);
        }
    }
}
