package com.example.CoopCredit.domain.port.out; // Define el paquete para los puertos de salida del dominio.

import com.example.CoopCredit.domain.model.user.Role; // Importa el enum Role del dominio.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.RoleEntity;

import java.util.Optional; // Importa Optional para manejar la posible ausencia de un resultado.

public interface RoleRepositoryPort { // Define la interfaz del puerto para la gestión de roles.

    Optional<Role> findByName(Role name); // Mét0do para buscar un rol por su nombre (enum Role).
    RoleEntity save(RoleEntity role); // Métod0 para guardar un RoleEntity.
}
