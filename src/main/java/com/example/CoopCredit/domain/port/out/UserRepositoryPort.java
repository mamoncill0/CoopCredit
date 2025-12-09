package com.example.CoopCredit.domain.port.out; // Define el paquete para los puertos de salida del dominio.

import com.example.CoopCredit.domain.model.user.User; // Importa el modelo de dominio User.

import java.util.Optional; // Importa Optional para manejar la posible ausencia de un resultado.

public interface UserRepositoryPort { // Define la interfaz del puerto para la gestión de usuarios.

    Optional<User> findByUsername(String username); // Método para buscar un usuario por su nombre de usuario.
    Boolean existsByUsername(String username); // Método para verificar si un nombre de usuario ya existe.
    Boolean existsByEmail(String email); // Método para verificar si un correo electrónico ya existe.
    User save(User user); // Método para guardar un usuario.
}
