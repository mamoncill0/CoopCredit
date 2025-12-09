package com.example.CoopCredit.infrastructure.config.security; // Define el paquete para los componentes de seguridad en la capa de infraestructura.

import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.UserEntity; // Importa la entidad UserEntity de persistencia.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository.UserJpaRepository; // Importa el repositorio JPA para UserEntity.
import org.springframework.beans.factory.annotation.Autowired; // Importa la anotación para inyección de dependencias.
import org.springframework.security.core.userdetails.UserDetails; // Importa la interfaz UserDetails.
import org.springframework.security.core.userdetails.UserDetailsService; // Importa la interfaz UserDetailsService.
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Importa la excepción para usuario no encontrado.
import org.springframework.stereotype.Service; // Importa la anotación @Service.
import org.springframework.transaction.annotation.Transactional; // Importa la anotación @Transactional.

@Service // Marca esta clase como un servicio de Spring.
public class UserDetailsServiceImpl implements UserDetailsService { // Implementa UserDetailsService para cargar usuarios.

    @Autowired // Inyecta el UserJpaRepository.
    UserJpaRepository userJpaRepository; // Repositorio JPA para acceder a los datos de UserEntity.

    @Override // Sobrescribe el mét0do loadUserByUsername de la interfaz UserDetailsService.
    @Transactional // Asegura que la operación de base de datos se realice dentro de una transacción.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // Carga un usuario por su nombre de usuario.
        UserEntity userEntity = userJpaRepository.findByUsername(username) // Busca el UserEntity en la base de datos.
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username)); // Lanza excepción si no se encuentra el usuario.

        return userEntity; // Retorna el objeto UserEntity (que implementa UserDetails).
    }
}
