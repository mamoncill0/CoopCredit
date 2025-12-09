package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.adapter; // Define el paquete para los adaptadores de persistencia.

import com.example.CoopCredit.domain.model.affiliate.Credit; // Importa el modelo de dominio Credit.
import com.example.CoopCredit.domain.model.affiliate.RiskEvaluation; // Importa el modelo de dominio RiskEvaluation.
import com.example.CoopCredit.domain.port.out.CreditRepositoryPort; // Importa el puerto de salida CreditRepositoryPort.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.AffiliateEntity; // Importa la entidad AffiliateEntity.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.CreditEntity; // Importa la entidad CreditEntity.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.RiskEvaluationEntity; // Importa la entidad RiskEvaluationEntity.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.mapper.CreditMapper; // Importa el CreditMapper.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository.CreditJpaRepository; // Importa el repositorio JPA para CreditEntity.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.stereotype.Component; // Importa @Component para que Spring detecte esta clase como un bean.

import java.util.List; // Importa List.
import java.util.Optional; // Importa Optional.
import java.util.stream.Collectors; // Importa Collectors.

@Component // Marca esta clase como un componente de Spring.
public class CreditPersistenceAdapter implements CreditRepositoryPort { // Implementa el puerto de salida CreditRepositoryPort.

    private final CreditJpaRepository creditJpaRepository; // Inyecta el repositorio JPA para CreditEntity.
    private final CreditMapper creditMapper; // Inyecta el CreditMapper.
    private final AffiliatePersistenceAdapter affiliatePersistenceAdapter; // Inyecta el adaptador de persistencia para Affiliate.
    private final RiskEvaluationPersistenceAdapter riskEvaluationPersistenceAdapter; // Inyecta el adaptador de persistencia para RiskEvaluation.

    @Autowired // Constructor para inyección de dependencias.
    public CreditPersistenceAdapter(CreditJpaRepository creditJpaRepository,
                                    CreditMapper creditMapper,
                                    AffiliatePersistenceAdapter affiliatePersistenceAdapter,
                                    RiskEvaluationPersistenceAdapter riskEvaluationPersistenceAdapter) {
        this.creditJpaRepository = creditJpaRepository;
        this.creditMapper = creditMapper;
        this.affiliatePersistenceAdapter = affiliatePersistenceAdapter;
        this.riskEvaluationPersistenceAdapter = riskEvaluationPersistenceAdapter;
    }

    @Override // Implementación del método save del puerto.
    public Credit save(Credit credit) {
        // 1. Mapear el Credit del dominio a CreditEntity.
        CreditEntity creditEntity = creditMapper.toEntity(credit);

        // 2. Asegurar que AffiliateEntity esté gestionada por JPA.
        // El CreditMapper ya usa affiliatePersistenceAdapter.toPersistenceAffiliateEntity(credit.getAffiliateDocument())
        // para obtener la AffiliateEntity. Aquí solo necesitamos asegurarnos de que la relación se establezca correctamente.
        // Si el Affiliate del dominio tiene un ID, asumimos que ya existe en la BD.
        // Si no, el AffiliatePersistenceAdapter.toPersistenceAffiliateEntity debería manejarlo.
        AffiliateEntity affiliateEntity = affiliatePersistenceAdapter.toPersistenceAffiliateEntity(credit.getAffiliateDocument());
        creditEntity.setAffiliate(affiliateEntity);

        // 3. Asegurar que RiskEvaluationEntity esté gestionada por JPA.
        // Si la evaluación de riesgo asociada al Credit del dominio no tiene ID, la guardamos primero.
        if (credit.getAssociatedEvaluation() != null && credit.getAssociatedEvaluation().getId() == null) {
            RiskEvaluation savedRiskEvaluation = riskEvaluationPersistenceAdapter.save(credit.getAssociatedEvaluation());
            creditEntity.setAssociatedEvaluation(riskEvaluationPersistenceAdapter.toPersistenceRiskEvaluationEntity(savedRiskEvaluation));
        } else if (credit.getAssociatedEvaluation() != null && credit.getAssociatedEvaluation().getId() != null) {
            // Si ya tiene ID, simplemente la mapeamos.
            creditEntity.setAssociatedEvaluation(riskEvaluationPersistenceAdapter.toPersistenceRiskEvaluationEntity(credit.getAssociatedEvaluation()));
        } else {
            creditEntity.setAssociatedEvaluation(null);
        }

        // 4. Guardar la CreditEntity.
        CreditEntity savedEntity = creditJpaRepository.save(creditEntity);

        // 5. Convertir la CreditEntity guardada de nuevo a Credit de dominio y retornarla.
        return creditMapper.toDomain(savedEntity);
    }

    @Override // Implementación del método findById del puerto.
    public Optional<Credit> findById(Long id) {
        return creditJpaRepository.findById(id).map(creditMapper::toDomain);
    }

    @Override // Implementación del método findAll del puerto.
    public List<Credit> findAll() {
        return creditJpaRepository.findAll().stream().map(creditMapper::toDomain).collect(Collectors.toList());
    }

    @Override // Implementación del método findByAffiliateDocument del puerto.
    public List<Credit> findByAffiliateDocument(Long affiliateDocument) {
        // Asumiendo que CreditJpaRepository.findByAffiliateDocument espera un Long.
        return creditJpaRepository.findByAffiliateDocument(affiliateDocument).stream()
                .map(creditMapper::toDomain)
                .collect(Collectors.toList());
    }
}
