package com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.dto; // Define el paquete para los DTOs de usuario en la capa de infraestructura.

import jakarta.validation.constraints.Email; // Importa la anotación para validar el formato de email.
import jakarta.validation.constraints.NotBlank; // Importa la anotación para validar que el campo no esté en blanco.
import jakarta.validation.constraints.Size; // Importa la anotación para validar el tamaño de la cadena.

import java.util.Set; // Importa Set para la colección de roles.

public class RegisterRequest { // Clase que representa la solicitud de registro de un nuevo usuario.

    @NotBlank // Asegura que el nombre de usuario no esté vacío.
    @Size(min = 3, max = 20) // Asegura que el nombre de usuario tenga entre 3 y 20 caracteres.
    private String username; // Campo para el nombre de usuario.

    @NotBlank // Asegura que el correo electrónico no esté vacío.
    @Size(max = 50) // Asegura que el correo electrónico tenga un máximo de 50 caracteres.
    @Email // Asegura que el formato del correo electrónico sea válido.
    private String email; // Campo para el correo electrónico.

    private Set<String> role; // Campo opcional para especificar los roles del usuario.

    @NotBlank // Asegura que la contraseña no esté vacía.
    @Size(min = 6, max = 40) // Asegura que la contraseña tenga entre 6 y 40 caracteres.
    private String password; // Campo para la contraseña.

    // Getters y Setters para los campos.

    public String getUsername() { // Método para obtener el nombre de usuario.
        return username; // Retorna el nombre de usuario.
    }

    public void setUsername(String username) { // Método para establecer el nombre de usuario.
        this.username = username; // Asigna el nombre de usuario.
    }

    public String getEmail() { // Método para obtener el correo electrónico.
        return email; // Retorna el correo electrónico.
    }

    public void setEmail(String email) { // Método para establecer el correo electrónico.
        this.email = email; // Asigna el correo electrónico.
    }

    public Set<String> getRole() { // Método para obtener los roles.
        return role; // Retorna el conjunto de roles.
    }

    public void setRole(Set<String> role) { // Método para establecer los roles.
        this.role = role; // Asigna el conjunto de roles.
    }

    public String getPassword() { // Método para obtener la contraseña.
        return password; // Retorna la contraseña.
    }

    public void setPassword(String password) { // Método para establecer la contraseña.
        this.password = password; // Asigna la contraseña.
    }
}
