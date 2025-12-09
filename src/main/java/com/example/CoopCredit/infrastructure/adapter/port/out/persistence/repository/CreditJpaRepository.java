package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository; // Define el paquete para los repositorios JPA.

import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.CreditEntity; // Importa la entidad CreditEntity.
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository para funcionalidades CRUD.
import org.springframework.stereotype.Repository; // Importa @Repository.

import java.util.List; // Importa List.

@Repository // Marca esta interfaz como un componente de repositorio de Spring.
public interface CreditJpaRepository extends JpaRepository<CreditEntity, Long> { // Extiende JpaRepository para CreditEntity con ID de tipo Long.

    // Método para buscar créditos por el documento del afiliado.
    List<CreditEntity> findByAffiliateDocument(Long affiliateDocument);
}
