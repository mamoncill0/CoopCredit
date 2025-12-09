package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.adapter; // Define el paquete para los adaptadores de persistencia.

import com.example.CoopCredit.domain.model.user.Role; // Importa el enum Role del dominio.
import com.example.CoopCredit.domain.model.user.User; // Importa el modelo de dominio User.
import com.example.CoopCredit.domain.port.out.UserRepositoryPort; // Importa el puerto de salida UserRepositoryPort.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.RoleEntity; // Importa la entidad RoleEntity de persistencia.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.UserEntity; // Importa la entidad UserEntity de persistencia.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository.RoleJpaRepository; // Importa el repositorio JPA para RoleEntity.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository.UserJpaRepository; // Importa el repositorio JPA para UserEntity.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.stereotype.Component; // Importa @Component para que Spring detecte esta clase como un bean.

import java.util.HashSet; // Importa HashSet.
import java.util.Optional; // Importa Optional para manejar resultados que pueden ser nulos.
import java.util.Set; // Importa Set para colecciones de roles.
import java.util.stream.Collectors; // Importa Collectors para operaciones de stream.

@Component // Marca esta clase como un componente de Spring.
public class UserPersistenceAdapter implements UserRepositoryPort { // Implementa el puerto de salida UserRepositoryPort.

    private final UserJpaRepository userJpaRepository; // Inyecta el repositorio JPA para UserEntity.
    private final RoleJpaRepository roleJpaRepository; // Inyecta el repositorio JPA para RoleEntity.

    @Autowired // Constructor para inyección de dependencias.
    public UserPersistenceAdapter(UserJpaRepository userJpaRepository, RoleJpaRepository roleJpaRepository) {
        this.userJpaRepository = userJpaRepository; // Asigna el repositorio JPA de usuarios.
        this.roleJpaRepository = roleJpaRepository; // Asigna el repositorio JPA de roles.
    }

    // Convierte un objeto UserEntity (persistencia) a un objeto User (dominio).
    private User toDomainUser(UserEntity userEntity) {
        User user = new User(userEntity.getUsername(), userEntity.getEmail(), userEntity.getPassword()); // Crea un nuevo User con datos básicos.
        user.setId(userEntity.getId()); // Establece el ID.
        Set<Role> roles = userEntity.getRoles().stream() // Convierte el Set de RoleEntity a Set de Role (enum).
                .map(RoleEntity::getName) // Mapea cada RoleEntity a su enum Role.
                .collect(Collectors.toCollection(HashSet::new)); // CORRECCIÓN: Recolecta en un HashSet mutable.
        user.setRoles(roles); // Establece los roles en el objeto User.
        return user; // Retorna el objeto User del dominio.
    }

    // Convierte un objeto User (dominio) a un objeto UserEntity (persistencia).
    private UserEntity toPersistenceUserEntity(User user) {
        UserEntity userEntity = new UserEntity(user.getUsername(), user.getEmail(), user.getPassword()); // Crea un nuevo UserEntity con datos básicos.
        userEntity.setId(user.getId()); // Establece el ID.
        Set<RoleEntity> roleEntities = user.getRoles().stream() // Convierte el Set de Role (enum) a Set de RoleEntity.
                .map(roleEnum -> roleJpaRepository.findByName(roleEnum) // Busca la RoleEntity existente por su nombre.
                        .orElseThrow(() -> new RuntimeException("Error: Role " + roleEnum + " not found in database."))) // Lanza excepción si el rol no existe.
                .collect(Collectors.toCollection(HashSet::new)); // CORRECCIÓN: Recolecta en un HashSet mutable.
        userEntity.setRoles(roleEntities); // Establece los roles en el objeto UserEntity.
        return userEntity; // Retorna el objeto UserEntity de persistencia.
    }

    @Override // Implementación del método findByUsername del puerto.
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username) // Busca en el repositorio JPA.
                .map(this::toDomainUser); // Si se encuentra, lo convierte a User de dominio.
    }

    @Override // Implementación del método existsByUsername del puerto.
    public Boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username); // Delega al repositorio JPA.
    }

    @Override // Implementación del método existsByEmail del puerto.
    public Boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email); // Delega al repositorio JPA.
    }

    @Override // Implementación del método save del puerto.
    public User save(User user) {
        UserEntity userEntity = toPersistenceUserEntity(user); // Convierte el User de dominio a UserEntity de persistencia.
        UserEntity savedUserEntity = userJpaRepository.save(userEntity); // Guarda la entidad en la base de datos.
        return toDomainUser(savedUserEntity); // Convierte la entidad guardada de nuevo a User de dominio y lo retorna.
    }
}
