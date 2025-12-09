package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository; // Define el paquete para los repositorios JPA.

import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.RiskEvaluationEntity; // Importa la entidad RiskEvaluationEntity.
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository para funcionalidades CRUD.
import org.springframework.stereotype.Repository; // Importa @Repository.

@Repository // Marca esta interfaz como un componente de repositorio de Spring.
public interface RiskEvaluationJpaRepository extends JpaRepository<RiskEvaluationEntity, Long> { // Extiende JpaRepository para RiskEvaluationEntity con ID de tipo Long.
}
