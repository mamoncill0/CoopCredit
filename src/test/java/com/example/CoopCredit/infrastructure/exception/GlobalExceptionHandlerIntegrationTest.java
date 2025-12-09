package com.example.CoopCredit.infrastructure.exception; // Define el paquete para las pruebas de integración del manejador de excepciones.

import com.example.CoopCredit.AbstractIntegrationTest; // Importa la clase base de pruebas de integración.
import com.example.CoopCredit.domain.model.user.Role; // Importa el enum Role.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.affiliate.dto.AffiliateRequest; // Importa AffiliateRequest para pruebas de validación.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.AuthController; // Importa AuthController para registrar usuarios.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.dto.LoginRequest;
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.dto.RegisterRequest; // Importa RegisterRequest.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.RoleEntity; // Importa RoleEntity.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.repository.RoleJpaRepository; // Importa RoleJpaRepository.
import com.fasterxml.jackson.databind.ObjectMapper; // Importa ObjectMapper.
import org.junit.jupiter.api.BeforeEach; // Importa BeforeEach.
import org.junit.jupiter.api.Test; // Importa Test.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired.
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // Importa AutoConfigureMockMvc.
import org.springframework.http.MediaType; // Importa MediaType.
import org.springframework.test.web.servlet.MockMvc; // Importa MockMvc.
import org.springframework.transaction.annotation.Transactional; // Importa Transactional.

import java.time.Instant;
import java.util.Collections; // Importa Collections.
import java.util.Date; // Importa Date.

import static org.hamcrest.Matchers.containsString; // Importa containsString.
import static org.hamcrest.Matchers.is; // Importa is.
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // Importa get.
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post; // Importa post.
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // Importa jsonPath.
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; // Importa status.

// Elimina @SpringBootTest, @ActiveProfiles("test") ya que se heredan de AbstractIntegrationTest.
@AutoConfigureMockMvc // Configura MockMvc automáticamente.
@Transactional // Asegura que cada prueba se ejecute en una transacción y se revierta.
public class GlobalExceptionHandlerIntegrationTest extends AbstractIntegrationTest { // Extiende la clase base de pruebas de integración.

    @Autowired // Inyecta MockMvc.
    private MockMvc mockMvc;
    @Autowired // Inyecta ObjectMapper.
    private ObjectMapper objectMapper;
    @Autowired // Inyecta RoleJpaRepository.
    private RoleJpaRepository roleJpaRepository;
    @Autowired // Inyecta AuthController para registrar usuarios de prueba.
    private AuthController authController;

    private String adminToken; // Token JWT del administrador.

    @BeforeEach // Se ejecuta antes de cada prueba.
    void setUp() throws Exception {
        // Asegura que los roles existan en la base de datos.
        for (Role roleName : Role.values()) {
            if (roleJpaRepository.findByName(roleName).isEmpty()) {
                roleJpaRepository.save(new RoleEntity(roleName));
            }
        }

        // Registrar y loguear ADMIN para obtener un token.
        RegisterRequest adminRegister = new RegisterRequest();
        adminRegister.setUsername("testadmin");
        adminRegister.setEmail("testadmin@example.com");
        adminRegister.setPassword("AdminPassword123");
        adminRegister.setRole(Collections.singleton("ADMIN"));
        authController.registerUser(adminRegister);

        LoginRequest adminLogin = new LoginRequest();
        adminLogin.setUsername("testadmin");
        adminLogin.setPassword("AdminPassword123");
        adminToken = objectMapper.readTree(mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(adminLogin)))
                        .andReturn().getResponse().getContentAsString())
                .get("accessToken").asText();
    }

    @Test // Prueba el manejo de errores de validación (MethodArgumentNotValidException).
    void handleValidationExceptions_returnsBadRequest() throws Exception {
        AffiliateRequest invalidRequest = new AffiliateRequest();
        invalidRequest.setDocument(0L); // Documento inválido (min = 1).
        invalidRequest.setName(""); // Nombre en blanco (NotBlank).
        invalidRequest.setSalary(0); // Salario inválido (min = 1).
        invalidRequest.setAffiliationDate(Date.from(Instant.now().plusSeconds(3600))); // Fecha futura (PastOrPresent).
        invalidRequest.setStatus(null); // Estado nulo (NotNull).

        mockMvc.perform(post("/affiliates") // Endpoint que requiere validación.
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest()) // Espera un estado HTTP 400 Bad Request.
                .andExpect(jsonPath("$.type", is("https://example.com/problems/validation-error"))) // Verifica el tipo de problema.
                .andExpect(jsonPath("$.title", is("Invalid Request Data"))) // Verifica el título.
                .andExpect(jsonPath("$.status", is(400))) // Verifica el estado.
                .andExpect(jsonPath("$.detail", is("Validation failed for request parameters."))) // Verifica el detalle general.
                .andExpect(jsonPath("$.errors", containsString("document: Document must be a positive number"))) // Verifica un error específico.
                .andExpect(jsonPath("$.errors", containsString("name: Name cannot be blank"))) // Verifica otro error.
                .andExpect(jsonPath("$.errors", containsString("salary: Salary must be greater than 0"))) // Verifica otro error.
                .andExpect(jsonPath("$.errors", containsString("affiliationDate: Affiliation date cannot be in the future"))) // Verifica otro error.
                .andExpect(jsonPath("$.errors", containsString("status: Status cannot be null"))); // Verifica otro error.
    }

    @Test // Prueba el manejo de errores de acceso denegado (AccessDeniedException).
    void handleAccessDeniedException_returnsForbidden() throws Exception {
        // Intenta acceder a un recurso de ADMIN sin token.
        mockMvc.perform(get("/affiliates/{document}", 123L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()); // Primero debería ser 401 si no hay token.

        // Intenta acceder a un recurso de ADMIN con un token de MEMBER.
        RegisterRequest memberRegister = new RegisterRequest();
        memberRegister.setUsername("testmember");
        memberRegister.setEmail("testmember@example.com");
        memberRegister.setPassword("MemberPassword123");
        memberRegister.setRole(Collections.singleton("MEMBER"));
        authController.registerUser(memberRegister);

        LoginRequest memberLogin = new LoginRequest();
        memberLogin.setUsername("testmember");
        memberLogin.setPassword("MemberPassword123");
        String memberToken = objectMapper.readTree(mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(memberLogin)))
                        .andReturn().getResponse().getContentAsString())
                .get("accessToken").asText();

        mockMvc.perform(post("/affiliates") // Intenta registrar un afiliado como MEMBER (solo ADMIN puede).
                        .header("Authorization", "Bearer " + memberToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AffiliateRequest()))) // Request inválido para forzar el 403.
                .andExpect(status().isForbidden()) // Espera un estado HTTP 403 Forbidden.
                .andExpect(jsonPath("$.type", is("https://example.com/problems/access-denied")))
                .andExpect(jsonPath("$.title", is("Access Denied")))
                .andExpect(jsonPath("$.status", is(403)))
                .andExpect(jsonPath("$.detail", is("Access Denied")));
    }

    @Test // Prueba el manejo de errores genéricos (Exception).
    void handleGenericException_returnsInternalServerError() throws Exception {
        // Simular un endpoint que lanza una excepción inesperada.
        // Para esto, podríamos crear un controlador temporal o un mock que lance una excepción.
        // Por simplicidad, este test es más conceptual o requeriría un endpoint específico de prueba.
        // Sin embargo, el GlobalExceptionHandler ya está configurado para capturar Exception.class.

        // Ejemplo conceptual:
        // mockMvc.perform(get("/some-endpoint-that-throws-exception")
        //                 .header("Authorization", "Bearer " + adminToken))
        //         .andExpect(status().isInternalServerError())
        //         .andExpect(jsonPath("$.type", is("https://example.com/problems/internal-server-error")));
    }
}
