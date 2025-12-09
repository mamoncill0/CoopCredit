package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository; // Define el paquete para los repositorios JPA.

import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.AffiliateEntity; // Importa la entidad AffiliateEntity.
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository para funcionalidades CRUD.
import org.springframework.stereotype.Repository; // Importa @Repository.

import java.util.Optional; // Importa Optional.

@Repository // Marca esta interfaz como un componente de repositorio de Spring.
public interface AffiliateJpaRepository extends JpaRepository<AffiliateEntity, Long> { // Extiende JpaRepository para AffiliateEntity con ID de tipo Long.

    Optional<AffiliateEntity> findByDocument(Long document); // Método para buscar un afiliado por su documento.
    boolean existsByDocument(Long document); // Método para verificar si un afiliado con un documento específico ya existe.
}
