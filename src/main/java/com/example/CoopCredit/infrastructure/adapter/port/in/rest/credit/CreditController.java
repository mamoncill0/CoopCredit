package com.example.CoopCredit.infrastructure.adapter.port.in.rest.credit; // Define el paquete para los controladores REST de crédito.

import com.example.CoopCredit.application.service.CreditService; // Importa el servicio de crédito de la capa de aplicación.
import com.example.CoopCredit.domain.model.affiliate.Credit; // Importa el modelo de dominio Credit.
import com.example.CoopCredit.domain.model.affiliate.RiskEvaluation; // Importa el modelo de dominio RiskEvaluation.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.credit.dto.CreditRequest; // Importa el DTO de solicitud de crédito.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.credit.dto.CreditResponse; // Importa el DTO de respuesta de crédito.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.risk.dto.RiskEvaluationResponse; // Importa el DTO de respuesta de evaluación de riesgo.
import jakarta.validation.Valid; // Importa @Valid para la validación de DTOs.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.http.HttpStatus; // Importa HttpStatus para códigos de estado HTTP.
import org.springframework.http.ResponseEntity; // Importa ResponseEntity para encapsular respuestas HTTP.
import org.springframework.security.access.prepost.PreAuthorize; // Importa @PreAuthorize para el control de acceso basado en roles.
import org.springframework.security.core.Authentication; // Importa Authentication de Spring Security.
import org.springframework.security.core.context.SecurityContextHolder; // Importa SecurityContextHolder para obtener el contexto de seguridad.
import org.springframework.web.bind.annotation.*; // Importa todas las anotaciones de Spring Web.

import java.util.List; // Importa List.
import java.util.stream.Collectors; // Importa Collectors.

@RestController // Marca esta clase como un controlador REST.
@RequestMapping("/credits") // Mapea todas las peticiones que comiencen con /credits a este controlador.
public class CreditController { // Clase controladora para la gestión de solicitudes de crédito.

    private final CreditService creditService; // Inyecta el CreditService de la capa de aplicación.

    @Autowired // Constructor para inyección de dependencias.
    public CreditController(CreditService creditService) {
        this.creditService = creditService; // Asigna el CreditService inyectado.
    }

    // Método auxiliar para convertir un Credit de dominio a CreditResponse.
    private CreditResponse toCreditResponse(Credit credit) {
        RiskEvaluationResponse riskResponse = null;
        if (credit.getAssociatedEvaluation() != null) {
            RiskEvaluation re = credit.getAssociatedEvaluation();
            riskResponse = new RiskEvaluationResponse(
                    credit.getAffiliateDocument().getDocument().toString(), // Asumiendo que Affiliate tiene getDocument()
                    re.getScore(),
                    re.getRiskLevel().name(), // Convierte el enum a String
                    re.getDecisionReason()
            );
        }
        return new CreditResponse(
                credit.getId(),
                credit.getAffiliateDocument().getDocument(), // Asumiendo que Affiliate tiene getDocument()
                credit.getAmount(),
                credit.getTerm(),
                credit.getProposedRate(),
                credit.getRequestDate(),
                credit.getStatusCredit(),
                riskResponse
        );
    }

    @PostMapping // Mapea las peticiones POST a /credits.
    @PreAuthorize("hasRole('MEMBER')") // Solo los usuarios con rol MEMBER pueden solicitar un crédito.
    public ResponseEntity<CreditResponse> requestCredit(@Valid @RequestBody CreditRequest request) {
        Credit credit = creditService.requestCredit(
                request.getAffiliateDocument(),
                request.getAmount(),
                request.getTerm(),
                request.getProposedRate()
        );
        return new ResponseEntity<>(toCreditResponse(credit), HttpStatus.CREATED); // Retorna la respuesta con estado 201 Created.
    }

    @GetMapping("/{id}") // Mapea las peticiones GET a /credits/{id}.
    @PreAuthorize("hasRole('ADMIN') or (hasRole('MEMBER') and @creditService.isCreditOwnedByAuthenticatedUser(#id, authentication.principal.username))") // ADMIN o MEMBER si es su propio crédito.
    public ResponseEntity<CreditResponse> getCreditById(@PathVariable Long id) {
        return creditService.getCreditById(id)
                .map(this::toCreditResponse)
                .map(creditResponse -> new ResponseEntity<>(creditResponse, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Retorna 404 si no se encuentra.
    }

    @GetMapping // Mapea las peticiones GET a /credits.
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede ver todos los créditos.
    public ResponseEntity<List<CreditResponse>> getAllCredits() {
        List<CreditResponse> credits = creditService.getAllCredits().stream()
                .map(this::toCreditResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(credits, HttpStatus.OK);
    }

    @GetMapping("/pending") // Mapea las peticiones GET a /credits/pending.
    @PreAuthorize("hasRole('ANALYST') or hasRole('ADMIN')") // ANALYST y ADMIN pueden ver solicitudes pendientes.
    public ResponseEntity<List<CreditResponse>> getPendingCredits() {
        List<CreditResponse> credits = creditService.getPendingCredits().stream()
                .map(this::toCreditResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(credits, HttpStatus.OK);
    }

    @PutMapping("/{id}/approve") // Mapea las peticiones PUT a /credits/{id}/approve.
    @PreAuthorize("hasRole('ANALYST') or hasRole('ADMIN')") // ANALYST y ADMIN pueden aprobar créditos.
    public ResponseEntity<CreditResponse> approveCredit(@PathVariable Long id, @RequestBody(required = false) String decisionReason) {
        Credit credit = creditService.approveCredit(id, decisionReason);
        return new ResponseEntity<>(toCreditResponse(credit), HttpStatus.OK);
    }

    @PutMapping("/{id}/reject") // Mapea las peticiones PUT a /credits/{id}/reject.
    @PreAuthorize("hasRole('ANALYST') or hasRole('ADMIN')") // ANALYST y ADMIN pueden rechazar créditos.
    public ResponseEntity<CreditResponse> rejectCredit(@PathVariable Long id, @RequestBody(required = false) String decisionReason) {
        Credit credit = creditService.rejectCredit(id, decisionReason);
        return new ResponseEntity<>(toCreditResponse(credit), HttpStatus.OK);
    }
}
