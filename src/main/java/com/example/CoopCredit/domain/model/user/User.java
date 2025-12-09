package com.example.CoopCredit.domain.model.user; // Define el paquete para los modelos de usuario del dominio.

import java.util.HashSet; // Importa HashSet para la colección de roles.
import java.util.Set; // Importa Set para la colección de roles.

public class User { // Clase de modelo de dominio para un usuario.

    private Long id; // Identificador único del usuario.
    private String username; // Nombre de usuario para el login.
    private String email; // Correo electrónico del usuario.
    private String password; // Contraseña del usuario.
    private Set<Role> roles = new HashSet<>(); // Conjunto de roles asociados a este usuario.

    // Constructor por defecto.
    public User() {
    }

    // Constructor para crear una instancia de User con datos básicos.
    public User(String username, String email, String password) {
        this.username = username; // Inicializa el nombre de usuario.
        this.email = email; // Inicializa el correo electrónico.
        this.password = password; // Inicializa la contraseña.
    }

    // Getters y Setters para los campos.

    public Long getId() { // Método para obtener el ID.
        return id; // Retorna el ID.
    }

    public void setId(Long id) { // Método para establecer el ID.
        this.id = id; // Asigna el ID.
    }

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

    public String getPassword() { // Método para obtener la contraseña.
        return password; // Retorna la contraseña.
    }

    public void setPassword(String password) { // Método para establecer la contraseña.
        this.password = password; // Asigna la contraseña.
    }

    public Set<Role> getRoles() { // Método para obtener el conjunto de roles.
        return roles; // Retorna los roles.
    }

    public void setRoles(Set<Role> roles) { // Método para establecer el conjunto de roles.
        this.roles = roles; // Asigna los roles.
    }
}
