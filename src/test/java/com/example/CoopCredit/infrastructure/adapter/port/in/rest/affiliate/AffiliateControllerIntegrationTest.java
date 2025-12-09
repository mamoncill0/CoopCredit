package com.example.CoopCredit.infrastructure.adapter.port.in.rest.affiliate; // Define el paquete para las pruebas de integración del controlador.

import com.example.CoopCredit.AbstractIntegrationTest; // Importa la clase base de pruebas de integración.
import com.example.CoopCredit.domain.model.affiliate.Status; // Importa el enum Status del dominio.
import com.example.CoopCredit.domain.model.user.Role; // Importa el enum Role del dominio.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.affiliate.dto.AffiliateRequest; // Importa el DTO de solicitud de afiliado.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.AuthController; // Importa AuthController para registrar usuarios.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.dto.LoginRequest; // Importa LoginRequest.
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

import java.time.Instant; // Importa Instant.
import java.util.Collections; // Importa Collections.
import java.util.Date; // Importa Date.

import static org.hamcrest.Matchers.is; // Importa is.
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // Importa métodos de MockMvcRequestBuilders.
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // Importa jsonPath.
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; // Importa status.

// Elimina @SpringBootTest, @ActiveProfiles("test") ya que se heredan de AbstractIntegrationTest.
@AutoConfigureMockMvc // Configura MockMvc automáticamente.
@Transactional // Asegura que cada prueba se ejecute en una transacción y se revierta.
public class AffiliateControllerIntegrationTest extends AbstractIntegrationTest { // Extiende la clase base de pruebas de integración.

    @Autowired // Inyecta MockMvc.
    private MockMvc mockMvc;
    @Autowired // Inyecta ObjectMapper.
    private ObjectMapper objectMapper;
    @Autowired // Inyecta RoleJpaRepository.
    private RoleJpaRepository roleJpaRepository;
    @Autowired // Inyecta AuthController para registrar usuarios de prueba.
    private AuthController authController;

    private String adminToken; // Token JWT del administrador.
    private String memberToken; // Token JWT del miembro.

    @BeforeEach // Se ejecuta antes de cada prueba.
    void setUp() throws Exception {
        // Asegura que los roles existan en la base de datos.
        for (Role roleName : Role.values()) {
            if (roleJpaRepository.findByName(roleName).isEmpty()) {
                roleJpaRepository.save(new RoleEntity(roleName));
            }
        }

        // 1. Registrar y loguear ADMIN
        RegisterRequest adminRegister = new RegisterRequest();
        adminRegister.setUsername("testadmin");
        adminRegister.setEmail("testadmin@example.com");
        adminRegister.setPassword("AdminPassword123");
        adminRegister.setRole(Collections.singleton("ADMIN"));
        authController.registerUser(adminRegister); // Registra el admin.

        LoginRequest adminLogin = new LoginRequest();
        adminLogin.setUsername("testadmin");
        adminLogin.setPassword("AdminPassword123");
        adminToken = objectMapper.readTree(mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(adminLogin)))
                        .andReturn().getResponse().getContentAsString())
                .get("accessToken").asText(); // Obtiene el token del admin.

        // 2. Registrar y loguear MEMBER
        RegisterRequest memberRegister = new RegisterRequest();
        memberRegister.setUsername("103"); // Documento del afiliado.
        memberRegister.setEmail("testmember@example.com");
        memberRegister.setPassword("MemberPassword123");
        memberRegister.setRole(Collections.singleton("MEMBER"));
        authController.registerUser(memberRegister); // Registra el miembro.

        LoginRequest memberLogin = new LoginRequest();
        memberLogin.setUsername("103");
        memberLogin.setPassword("MemberPassword123");
        memberToken = objectMapper.readTree(mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(memberLogin)))
                        .andReturn().getResponse().getContentAsString())
                .get("accessToken").asText(); // Obtiene el token del miembro.
    }

    @Test // Prueba el registro de un afiliado exitoso como ADMIN.
    void registerAffiliate_asAdmin_success() throws Exception {
        AffiliateRequest request = new AffiliateRequest();
        request.setDocument(123L);
        request.setName("New Affiliate");
        request.setSalary(100000);
        request.setAffiliationDate(Date.from(Instant.now()));
        request.setStatus(Status.Active);

        mockMvc.perform(post("/affiliates") // Realiza una petición POST a /affiliates.
                        .header("Authorization", "Bearer " + adminToken) // Añade el token del admin.
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // Espera un estado HTTP 201 Created.
                .andExpect(jsonPath("$.document", is(123))); // Verifica el documento del afiliado.
    }

    @Test // Prueba el registro de un afiliado denegado como MEMBER.
    void registerAffiliate_asMember_forbidden() throws Exception {
        AffiliateRequest request = new AffiliateRequest();
        request.setDocument(123L);
        request.setName("New Affiliate");
        request.setSalary(100000);
        request.setAffiliationDate(Date.from(Instant.now()));
        request.setStatus(Status.Active);

        mockMvc.perform(post("/affiliates")
                        .header("Authorization", "Bearer " + memberToken) // Añade el token del miembro.
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden()); // Espera un estado HTTP 403 Forbidden.
    }

    @Test // Prueba la obtención de un afiliado por ID como ADMIN.
    void getAffiliateById_asAdmin_success() throws Exception {
        // Primero registra un afiliado como admin.
        registerAffiliate_asAdmin_success();

        mockMvc.perform(get("/affiliates/{document}", 123L) // Realiza una petición GET a /affiliates/{document}.
                        .header("Authorization", "Bearer " + adminToken) // Añade el token del admin.
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera un estado HTTP 200 OK.
                .andExpect(jsonPath("$.document", is(123))); // Verifica el documento.
    }

    @Test // Prueba la obtención de su propio afiliado por ID como MEMBER.
    void getAffiliateById_asMember_ownAffiliate_success() throws Exception {
        // Primero registra el afiliado para el miembro (documento 103).
        AffiliateRequest request = new AffiliateRequest();
        request.setDocument(103L);
        request.setName("Test Member Affiliate");
        request.setSalary(100000);
        request.setAffiliationDate(Date.from(Instant.now()));
        request.setStatus(Status.Active);
        mockMvc.perform(post("/affiliates")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/affiliates/{document}", 103L) // Realiza una petición GET a /affiliates/{document}.
                        .header("Authorization", "Bearer " + memberToken) // Añade el token del miembro.
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera un estado HTTP 200 OK.
                .andExpect(jsonPath("$.document", is(103))); // Verifica el documento.
    }

    @Test // Prueba la obtención de un afiliado ajeno por ID como MEMBER.
    void getAffiliateById_asMember_otherAffiliate_forbidden() throws Exception {
        // Primero registra un afiliado diferente.
        AffiliateRequest request = new AffiliateRequest();
        request.setDocument(999L);
        request.setName("Other Affiliate");
        request.setSalary(100000);
        request.setAffiliationDate(Date.from(Instant.now()));
        request.setStatus(Status.Active);
        mockMvc.perform(post("/affiliates")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/affiliates/{document}", 999L) // Intenta obtener el afiliado ajeno.
                        .header("Authorization", "Bearer " + memberToken) // Añade el token del miembro.
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()); // Espera un estado HTTP 403 Forbidden.
    }

    @Test // Prueba la actualización de un afiliado como ADMIN.
    void updateAffiliate_asAdmin_success() throws Exception {
        // Primero registra un afiliado.
        registerAffiliate_asAdmin_success();

        AffiliateRequest updateRequest = new AffiliateRequest();
        updateRequest.setDocument(123L);
        updateRequest.setName("Updated Affiliate Name");
        updateRequest.setSalary(120000);
        updateRequest.setAffiliationDate(Date.from(Instant.now()));
        updateRequest.setStatus(Status.Inactive);

        mockMvc.perform(put("/affiliates/{document}", 123L) // Realiza una petición PUT a /affiliates/{document}.
                        .header("Authorization", "Bearer " + adminToken) // Añade el token del admin.
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk()) // Espera un estado HTTP 200 OK.
                .andExpect(jsonPath("$.name", is("Updated Affiliate Name"))) // Verifica el nombre actualizado.
                .andExpect(jsonPath("$.salary", is(120000))) // Verifica el salario actualizado.
                .andExpect(jsonPath("$.status", is("Inactive"))); // Verifica el estado actualizado.
    }

    @Test // Prueba la actualización de un afiliado denegado como MEMBER.
    void updateAffiliate_asMember_forbidden() throws Exception {
        // Primero registra un afiliado.
        registerAffiliate_asAdmin_success();

        AffiliateRequest updateRequest = new AffiliateRequest();
        updateRequest.setDocument(123L);
        updateRequest.setName("Updated Affiliate Name");
        updateRequest.setSalary(120000);
        updateRequest.setAffiliationDate(Date.from(Instant.now()));
        updateRequest.setStatus(Status.Inactive);

        mockMvc.perform(put("/affiliates/{document}", 123L)
                        .header("Authorization", "Bearer " + memberToken) // Añade el token del miembro.
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden()); // Espera un estado HTTP 403 Forbidden.
    }
}
