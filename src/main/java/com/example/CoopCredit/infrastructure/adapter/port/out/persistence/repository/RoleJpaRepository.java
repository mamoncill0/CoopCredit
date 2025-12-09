package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository; // Define el paquete para los repositorios JPA en la capa de infraestructura.

import com.example.CoopCredit.domain.model.user.Role; // Importa el enum Role del dominio.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.RoleEntity; // Importa la entidad de rol de persistencia.
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository para funcionalidades CRUD básicas.
import org.springframework.stereotype.Repository; // Importa la anotación @Repository para marcar la interfaz como un bean de repositorio.

import java.util.Optional; // Importa Optional para manejar resultados que pueden ser nulos.

@Repository // Marca esta interfaz como un componente de repositorio de Spring.
public interface RoleJpaRepository extends JpaRepository<RoleEntity, Integer> { // Extiende JpaRepository para la entidad RoleEntity con un ID de tipo Integer.

    // Declara un método para buscar un rol por su nombre (utilizando el enum Role del dominio).
    // Spring Data JPA implementará automáticamente este método.
    Optional<RoleEntity> findByName(Role name);
}
