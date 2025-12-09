package com.example.CoopCredit.infrastructure.adapter.port.in.rest.user; // Define el paquete para los controladores REST de usuario.

import com.example.CoopCredit.application.service.AuthService; // Importa el servicio de autenticación de la capa de aplicación.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.dto.AuthResponse; // Importa el DTO de respuesta de autenticación.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.dto.LoginRequest; // Importa el DTO de solicitud de login.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.dto.RegisterRequest; // Importa el DTO de solicitud de registro.
import jakarta.validation.Valid; // Importa @Valid para la validación de DTOs.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.http.ResponseEntity; // Importa ResponseEntity para encapsular respuestas HTTP.
import org.springframework.web.bind.annotation.*; // Importa todas las anotaciones de Spring Web.
import org.springframework.web.server.ResponseStatusException; // Importa ResponseStatusException para manejar errores HTTP.

@RestController // Marca esta clase como un controlador REST.
@RequestMapping("/auth") // Mapea todas las peticiones que comiencen con /auth a este controlador.
public class AuthController { // Clase controladora para la autenticación de usuarios.

    private final AuthService authService; // Inyecta el AuthService de la capa de aplicación.

    @Autowired // Constructor para inyección de dependencias.
    public AuthController(AuthService authService) {
        this.authService = authService; // Asigna el AuthService inyectado.
    }

    @PostMapping("/login") // Mapea las peticiones POST a /auth/login.
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) { // Método para autenticar usuarios.
        // Delega la lógica de autenticación al AuthService.
        AuthResponse authResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(authResponse); // Retorna la respuesta de autenticación.
    }

    @PostMapping("/register") // Mapea las peticiones POST a /auth/register.
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) { // Método para registrar nuevos usuarios.
        try {
            // Delega la lógica de registro al AuthService.
            authService.registerUser(signUpRequest);
            return ResponseEntity.ok("User registered successfully!"); // Retorna un mensaje de éxito.
        } catch (ResponseStatusException ex) {
            // Captura excepciones lanzadas por el AuthService y retorna la respuesta HTTP adecuada.
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }
}
