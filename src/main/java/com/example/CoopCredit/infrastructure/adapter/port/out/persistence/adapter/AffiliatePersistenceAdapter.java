package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.adapter; // Define el paquete para los adaptadores de persistencia.

import com.example.CoopCredit.domain.model.affiliate.Affiliate; // Importa el modelo de dominio Affiliate.
import com.example.CoopCredit.domain.model.affiliate.Status; // Importa el enum Status del dominio.
import com.example.CoopCredit.domain.port.out.AffiliateRepositoryPort; // Importa el puerto de salida AffiliateRepositoryPort.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.AffiliateEntity; // Importa la entidad AffiliateEntity de persistencia.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository.AffiliateJpaRepository; // Importa el repositorio JPA para AffiliateEntity.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.stereotype.Component; // Importa @Component para que Spring detecte esta clase como un bean.

import java.util.Optional; // Importa Optional para manejar resultados que pueden ser nulos.

@Component // Marca esta clase como un componente de Spring.
public class AffiliatePersistenceAdapter implements AffiliateRepositoryPort { // Implementa el puerto de salida AffiliateRepositoryPort.

    private final AffiliateJpaRepository affiliateJpaRepository; // Inyecta el repositorio JPA para AffiliateEntity.

    @Autowired // Constructor para inyección de dependencias.
    public AffiliatePersistenceAdapter(AffiliateJpaRepository affiliateJpaRepository) {
        this.affiliateJpaRepository = affiliateJpaRepository; // Asigna el repositorio JPA de afiliados.
    }

    // Convierte un objeto AffiliateEntity (persistencia) a un objeto Affiliate (dominio).
    public Affiliate toDomainAffiliate(AffiliateEntity affiliateEntity) { // CORRECCIÓN: Visibilidad cambiada a public.
        Affiliate affiliate = new Affiliate(
                affiliateEntity.getDocument(),
                affiliateEntity.getName(),
                affiliateEntity.getSalary(),
                affiliateEntity.getAffiliationDate(),
                affiliateEntity.getStatus()
        );
        // Aquí podrías añadir más mapeos si el modelo de dominio Affiliate tuviera más lógica o campos.
        return affiliate; // Retorna el objeto Affiliate del dominio.
    }

    // Convierte un objeto Affiliate (dominio) a un objeto AffiliateEntity (persistencia).
    public AffiliateEntity toPersistenceAffiliateEntity(Affiliate affiliate) { // CORRECCIÓN: Visibilidad cambiada a public.
        AffiliateEntity affiliateEntity = new AffiliateEntity(
                affiliate.getDocument(),
                affiliate.getName(),
                affiliate.getSalary(),
                affiliate.getAffiliationDate(),
                affiliate.getStatus()
        );
        return affiliateEntity; // Retorna el objeto AffiliateEntity de persistencia.
    }

    @Override // Implementación del método findByDocument del puerto.
    public Optional<Affiliate> findByDocument(Long document) {
        return affiliateJpaRepository.findByDocument(document) // Busca en el repositorio JPA.
                .map(this::toDomainAffiliate); // Si se encuentra, lo convierte a Affiliate de dominio.
    }

    @Override // Implementación del método save del puerto.
    public Affiliate save(Affiliate affiliate) {
        AffiliateEntity affiliateEntity = toPersistenceAffiliateEntity(affiliate); // Convierte el Affiliate de dominio a AffiliateEntity de persistencia.
        AffiliateEntity savedAffiliateEntity = affiliateJpaRepository.save(affiliateEntity); // Guarda la entidad en la base de datos.
        return toDomainAffiliate(savedAffiliateEntity); // Convierte la entidad guardada de nuevo a Affiliate de dominio y lo retorna.
    }

    @Override // Implementación del método existsByDocument del puerto.
    public boolean existsByDocument(Long document) {
        return affiliateJpaRepository.existsByDocument(document); // Delega al repositorio JPA.
    }
}
