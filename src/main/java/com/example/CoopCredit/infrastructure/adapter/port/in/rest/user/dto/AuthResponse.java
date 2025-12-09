package com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.dto; // Define el paquete para los DTOs de usuario en la capa de infraestructura.

public class AuthResponse { // Clase que representa la respuesta de autenticación.

    private String token; // Campo para el token JWT generado.
    private String type = "Bearer"; // Tipo de token, por defecto "Bearer".
    private Long id; // ID del usuario.
    private String username; // Nombre de usuario.
    private String email; // Correo electrónico del usuario.
    private String role; // Rol del usuario (simplificado a una cadena para la respuesta).

    // Constructor para inicializar la respuesta de autenticación.
    public AuthResponse(String accessToken, Long id, String username, String email, String role) {
        this.token = accessToken; // Asigna el token de acceso.
        this.id = id; // Asigna el ID del usuario.
        this.username = username; // Asigna el nombre de usuario.
        this.email = email; // Asigna el correo electrónico.
        this.role = role; // Asigna el rol del usuario.
    }

    // Getters y Setters para los campos.

    public String getAccessToken() { // Método para obtener el token de acceso.
        return token; // Retorna el token.
    }

    public void setAccessToken(String accessToken) { // Método para establecer el token de acceso.
        this.token = accessToken; // Asigna el token.
    }

    public String getTokenType() { // Método para obtener el tipo de token.
        return type; // Retorna el tipo de token.
    }

    public void setTokenType(String tokenType) { // Método para establecer el tipo de token.
        this.type = tokenType; // Asigna el tipo de token.
    }

    public Long getId() { // Método para obtener el ID del usuario.
        return id; // Retorna el ID.
    }

    public void setId(Long id) { // Método para establecer el ID del usuario.
        this.id = id; // Asigna el ID.
    }

    public String getEmail() { // Método para obtener el correo electrónico.
        return email; // Retorna el correo electrónico.
    }

    public void setEmail(String email) { // Método para establecer el correo electrónico.
        this.email = email; // Asigna el correo electrónico.
    }

    public String getUsername() { // Método para obtener el nombre de usuario.
        return username; // Retorna el nombre de usuario.
    }

    public void setUsername(String username) { // Método para establecer el nombre de usuario.
        this.username = username; // Asigna el nombre de usuario.
    }

    public String getRole() { // Método para obtener el rol del usuario.
        return role; // Retorna el rol.
    }

    public void setRole(String role) { // Método para establecer el rol del usuario.
        this.role = role; // Asigna el rol.
    }
}
