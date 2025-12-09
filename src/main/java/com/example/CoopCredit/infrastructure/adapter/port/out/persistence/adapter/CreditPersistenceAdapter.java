package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.adapter; // Define el paquete para los adaptadores de persistencia.

import com.example.CoopCredit.domain.model.affiliate.Credit; // Importa el modelo de dominio Credit.
import com.example.CoopCredit.domain.model.affiliate.RiskEvaluation; // Importa el modelo de dominio RiskEvaluation.
import com.example.CoopCredit.domain.port.out.CreditRepositoryPort; // Importa el puerto de salida CreditRepositoryPort.
import com.example.CoopCredit.domain.port.out.AffiliateRepositoryPort; // Importa el puerto de salida AffiliateRepositoryPort.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.AffiliateEntity; // Importa la entidad AffiliateEntity.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.CreditEntity; // Importa la entidad CreditEntity.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.RiskEvaluationEntity; // Importa la entidad RiskEvaluationEntity.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository.CreditJpaRepository; // Importa el repositorio JPA para CreditEntity.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.stereotype.Component; // Importa @Component para que Spring detecte esta clase como un bean.

import java.util.List; // Importa List.
import java.util.Optional; // Importa Optional.
import java.util.stream.Collectors; // Importa Collectors.

@Component // Marca esta clase como un componente de Spring.
public class CreditPersistenceAdapter implements CreditRepositoryPort { // Implementa el puerto de salida CreditRepositoryPort.

    private final CreditJpaRepository creditJpaRepository; // Inyecta el repositorio JPA para CreditEntity.
    private final AffiliatePersistenceAdapter affiliatePersistenceAdapter; // Inyecta el adaptador de persistencia para Affiliate.
    private final RiskEvaluationPersistenceAdapter riskEvaluationPersistenceAdapter; // Inyecta el adaptador de persistencia para RiskEvaluation.

    @Autowired // Constructor para inyección de dependencias.
    public CreditPersistenceAdapter(CreditJpaRepository creditJpaRepository,
                                    AffiliatePersistenceAdapter affiliatePersistenceAdapter,
                                    RiskEvaluationPersistenceAdapter riskEvaluationPersistenceAdapter) {
        this.creditJpaRepository = creditJpaRepository; // Asigna el repositorio JPA de créditos.
        this.affiliatePersistenceAdapter = affiliatePersistenceAdapter; // Asigna el adaptador de persistencia de afiliados.
        this.riskEvaluationPersistenceAdapter = riskEvaluationPersistenceAdapter; // Asigna el adaptador de persistencia de evaluaciones de riesgo.
    }

    // Convierte un objeto CreditEntity (persistencia) a un objeto Credit (dominio).
    private Credit toDomainCredit(CreditEntity entity) {
        Credit credit = new Credit(
                affiliatePersistenceAdapter.toDomainAffiliate(entity.getAffiliate()), // Convierte AffiliateEntity a Affiliate.
                entity.getAmount(),
                entity.getTerm(),
                entity.getProposedRate()
        );
        credit.setId(entity.getId());
        credit.setRequestDate(entity.getRequestDate());
        credit.setStatusCredit(entity.getStatusCredit());
        if (entity.getAssociatedEvaluation() != null) {
            credit.setAssociatedEvaluation(riskEvaluationPersistenceAdapter.toDomainRiskEvaluation(entity.getAssociatedEvaluation())); // Convierte RiskEvaluationEntity a RiskEvaluation.
        }
        return credit;
    }

    // Convierte un objeto Credit (dominio) a un objeto CreditEntity (persistencia).
    private CreditEntity toPersistenceCreditEntity(Credit domain) {
        AffiliateEntity affiliateEntity = affiliatePersistenceAdapter.toPersistenceAffiliateEntity(domain.getAffiliateDocument()); // Convierte Affiliate a AffiliateEntity.
        CreditEntity entity = new CreditEntity(
                affiliateEntity,
                domain.getAmount(),
                domain.getTerm(),
                domain.getProposedRate(),
                domain.getRequestDate(),
                domain.getStatusCredit()
        );
        entity.setId(domain.getId());
        if (domain.getAssociatedEvaluation() != null) {
            entity.setAssociatedEvaluation(riskEvaluationPersistenceAdapter.toPersistenceRiskEvaluationEntity(domain.getAssociatedEvaluation())); // Convierte RiskEvaluation a RiskEvaluationEntity.
        }
        return entity;
    }

    @Override // Implementación del método save del puerto.
    public Credit save(Credit credit) {
        CreditEntity entity = toPersistenceCreditEntity(credit);
        CreditEntity savedEntity = creditJpaRepository.save(entity);
        return toDomainCredit(savedEntity);
    }

    @Override // Implementación del método findById del puerto.
    public Optional<Credit> findById(Long id) {
        return creditJpaRepository.findById(id).map(this::toDomainCredit);
    }

    @Override // Implementación del método findAll del puerto.
    public List<Credit> findAll() {
        return creditJpaRepository.findAll().stream().map(this::toDomainCredit).collect(Collectors.toList());
    }

    @Override // Implementación del método findByAffiliateDocument del puerto.
    public List<Credit> findByAffiliateDocument(Long affiliateDocument) {
        return creditJpaRepository.findByAffiliateDocument(affiliateDocument).stream().map(this::toDomainCredit).collect(Collectors.toList());
    }
}
