package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository; // Define el paquete para los repositorios JPA en la capa de infraestructura.

import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.UserEntity; // Importa la entidad de usuario de persistencia.
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository para funcionalidades CRUD básicas.
import org.springframework.stereotype.Repository; // Importa la anotación @Repository para marcar la interfaz como un bean de repositorio.

import java.util.Optional; // Importa Optional para manejar resultados que pueden ser nulos.

@Repository // Marca esta interfaz como un componente de repositorio de Spring.
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> { // Extiende JpaRepository para la entidad UserEntity con un ID de tipo Long.

    // Declara un método para buscar un usuario por su nombre de usuario.
    // Spring Data JPA implementará automáticamente este método basándose en su nombre.
    Optional<UserEntity> findByUsername(String username);

    // Declara un método para verificar si un usuario con un nombre de usuario específico ya existe.
    Boolean existsByUsername(String username);

    // Declara un método para verificar si un usuario con un correo electrónico específico ya existe.
    Boolean existsByEmail(String email);
}
