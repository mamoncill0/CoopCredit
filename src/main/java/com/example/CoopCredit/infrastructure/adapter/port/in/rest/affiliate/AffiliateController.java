package com.example.CoopCredit.infrastructure.adapter.port.in.rest.affiliate; // Define el paquete para los controladores REST de afiliado.

import com.example.CoopCredit.application.service.AffiliateService; // Importa el servicio de afiliado de la capa de aplicación.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.affiliate.dto.AffiliateRequest; // Importa el DTO de solicitud de afiliado.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.affiliate.dto.AffiliateResponse; // Importa el DTO de respuesta de afiliado.
import jakarta.validation.Valid; // Importa @Valid para la validación de DTOs.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.http.HttpStatus; // Importa HttpStatus para códigos de estado HTTP.
import org.springframework.http.ResponseEntity; // Importa ResponseEntity para encapsular respuestas HTTP.
import org.springframework.security.access.prepost.PreAuthorize; // Importa @PreAuthorize para el control de acceso basado en roles.
import org.springframework.web.bind.annotation.*; // Importa todas las anotaciones de Spring Web.

@RestController // Marca esta clase como un controlador REST.
@RequestMapping("/affiliates") // Mapea todas las peticiones que comiencen con /affiliates a este controlador.
public class AffiliateController { // Clase controladora para la gestión de afiliados.

    private final AffiliateService affiliateService; // Inyecta el AffiliateService de la capa de aplicación.

    @Autowired // Constructor para inyección de dependencias.
    public AffiliateController(AffiliateService affiliateService) {
        this.affiliateService = affiliateService; // Asigna el AffiliateService inyectado.
    }

    @PostMapping // Mapea las peticiones POST a /affiliates.
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede registrar afiliados.
    public ResponseEntity<AffiliateResponse> registerAffiliate(@Valid @RequestBody AffiliateRequest request) {
        AffiliateResponse response = affiliateService.registerAffiliate(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED); // Retorna la respuesta con estado 201 Created.
    }

    @GetMapping("/{document}") // Mapea las peticiones GET a /affiliates/{document}.
    @PreAuthorize("hasRole('ADMIN') or (hasRole('MEMBER') and #document == authentication.principal.username)") // ADMIN o MEMBER si es su propio documento.
    public ResponseEntity<AffiliateResponse> getAffiliateByDocument(@PathVariable Long document) {
        return affiliateService.getAffiliateByDocument(document)
                .map(affiliateResponse -> new ResponseEntity<>(affiliateResponse, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Retorna 404 si no se encuentra.
    }

    @PutMapping("/{document}") // Mapea las peticiones PUT a /affiliates/{document}.
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede actualizar afiliados.
    public ResponseEntity<AffiliateResponse> updateAffiliate(@PathVariable Long document, @Valid @RequestBody AffiliateRequest request) {
        AffiliateResponse response = affiliateService.updateAffiliate(document, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
