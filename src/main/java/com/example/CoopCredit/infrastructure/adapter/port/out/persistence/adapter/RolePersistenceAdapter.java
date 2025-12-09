package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.adapter; // Define el paquete para los adaptadores de persistencia.

import com.example.CoopCredit.domain.model.user.Role; // Importa el enum Role del dominio.
import com.example.CoopCredit.domain.port.out.RoleRepositoryPort; // Importa el puerto de salida RoleRepositoryPort.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.RoleEntity; // Importa la entidad RoleEntity de persistencia.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository.RoleJpaRepository; // Importa el repositorio JPA para RoleEntity.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.stereotype.Component; // Importa @Component para que Spring detecte esta clase como un bean.

import java.util.Optional; // Importa Optional para manejar resultados que pueden ser nulos.

@Component // Marca esta clase como un componente de Spring.
public class RolePersistenceAdapter implements RoleRepositoryPort { // Implementa el puerto de salida RoleRepositoryPort.

    private final RoleJpaRepository roleJpaRepository; // Inyecta el repositorio JPA para RoleEntity.

    @Autowired // Constructor para inyección de dependencias.
    public RolePersistenceAdapter(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository; // Asigna el repositorio JPA.
    }

    @Override // Implementación del método findByName del puerto.
    public Optional<Role> findByName(Role name) {
        return roleJpaRepository.findByName(name) // Busca en el repositorio JPA por el nombre del enum Role.
                .map(RoleEntity::getName); // Si se encuentra, retorna el enum Role del RoleEntity.
    }

    @Override // Implementación del método save del puerto.
    public RoleEntity save(RoleEntity roleEntity) {
        return roleJpaRepository.save(roleEntity); // Guarda la entidad RoleEntity directamente en la base de datos.
    }
}
