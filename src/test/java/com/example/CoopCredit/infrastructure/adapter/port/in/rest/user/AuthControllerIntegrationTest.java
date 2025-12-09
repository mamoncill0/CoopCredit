package com.example.CoopCredit.infrastructure.adapter.port.in.rest.user; // Define el paquete para las pruebas de integración del controlador.

import com.example.CoopCredit.AbstractIntegrationTest; // Importa la clase base de pruebas de integración.
import com.example.CoopCredit.domain.model.user.Role; // Importa el enum Role del dominio.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.dto.LoginRequest; // Importa el DTO de solicitud de login.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.dto.RegisterRequest; // Importa el DTO de solicitud de registro.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.RoleEntity; // Importa la entidad RoleEntity.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository.RoleJpaRepository; // Importa el repositorio JPA de roles.
import com.fasterxml.jackson.databind.ObjectMapper; // Importa ObjectMapper para convertir objetos a JSON.
import org.junit.jupiter.api.BeforeEach; // Importa BeforeEach.
import org.junit.jupiter.api.Test; // Importa Test.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired.
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // Importa AutoConfigureMockMvc.
import org.springframework.http.MediaType; // Importa MediaType.
import org.springframework.test.web.servlet.MockMvc; // Importa MockMvc para realizar peticiones HTTP simuladas.
import org.springframework.transaction.annotation.Transactional; // Importa Transactional para gestionar transacciones en pruebas.

import java.util.Collections; // Importa Collections.

import static org.hamcrest.Matchers.is; // Importa is para aserciones de Hamcrest.
import static org.junit.jupiter.api.Assertions.assertNotNull; // Importa assertNotNull.
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post; // Importa post para construir peticiones POST.
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // Importa jsonPath para aserciones en JSON.
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; // Importa status para aserciones de estado HTTP.

// Elimina @SpringBootTest, @ActiveProfiles("test") ya que se heredan de AbstractIntegrationTest.
@AutoConfigureMockMvc // Configura MockMvc automáticamente.
@Transactional // Asegura que cada prueba se ejecute en una transacción y se revierta al finalizar.
public class AuthControllerIntegrationTest extends AbstractIntegrationTest { // Extiende la clase base de pruebas de integración.

    @Autowired // Inyecta MockMvc.
    private MockMvc mockMvc;
    @Autowired // Inyecta ObjectMapper.
    private ObjectMapper objectMapper;
    @Autowired // Inyecta RoleJpaRepository.
    private RoleJpaRepository roleJpaRepository;

    @BeforeEach // Se ejecuta antes de cada prueba.
    void setUp() {
        // Asegura que los roles existan en la base de datos antes de cada prueba.
        // Esto es importante porque @Transactional revierte los cambios.
        for (Role roleName : Role.values()) {
            if (roleJpaRepository.findByName(roleName).isEmpty()) {
                roleJpaRepository.save(new RoleEntity(roleName));
            }
        }
    }

    @Test // Prueba el registro de un usuario exitoso.
    void registerUser_success() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("SecurePassword123");
        registerRequest.setRole(Collections.singleton("MEMBER"));

        mockMvc.perform(post("/auth/register") // Realiza una petición POST a /auth/register.
                        .contentType(MediaType.APPLICATION_JSON) // Establece el tipo de contenido.
                        .content(objectMapper.writeValueAsString(registerRequest))) // Convierte el objeto a JSON.
                .andExpect(status().isOk()) // Espera un estado HTTP 200 OK.
                .andExpect(jsonPath("$", is("User registered successfully!"))); // Espera el mensaje de éxito.
    }

    @Test // Prueba el registro de un usuario con nombre de usuario ya existente.
    void registerUser_usernameExists_returnsBadRequest() throws Exception {
        // Primero registra un usuario.
        registerUser_success();

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser"); // Nombre de usuario ya existente.
        registerRequest.setEmail("another@example.com");
        registerRequest.setPassword("SecurePassword123");
        registerRequest.setRole(Collections.singleton("MEMBER"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest()) // Espera un estado HTTP 400 Bad Request.
                .andExpect(jsonPath("$.detail", is("Error: Username is already taken!"))); // Espera el mensaje de error.
    }

    @Test // Prueba el login de un usuario exitoso.
    void loginUser_success() throws Exception {
        // Primero registra un usuario.
        registerUser_success();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("newuser");
        loginRequest.setPassword("SecurePassword123");

        String responseContent = mockMvc.perform(post("/auth/login") // Realiza una petición POST a /auth/login.
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk()) // Espera un estado HTTP 200 OK.
                .andExpect(jsonPath("$.accessToken").exists()) // Espera que el token de acceso exista.
                .andExpect(jsonPath("$.username", is("newuser"))) // Verifica el nombre de usuario.
                .andReturn().getResponse().getContentAsString(); // Obtiene el contenido de la respuesta.

        assertNotNull(responseContent); // Asegura que la respuesta no sea nula.
    }

    @Test // Prueba el login de un usuario con credenciales inválidas.
    void loginUser_invalidCredentials_returnsUnauthorized() throws Exception {
        // No registra ningún usuario para asegurar que las credenciales son inválidas.
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistent");
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized()); // Espera un estado HTTP 401 Unauthorized.
    }
}
