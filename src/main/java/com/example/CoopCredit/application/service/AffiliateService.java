package com.example.CoopCredit.application.service; // Define el paquete para los servicios de la aplicación.

import com.example.CoopCredit.domain.model.affiliate.Affiliate; // Importa el modelo de dominio Affiliate.
import com.example.CoopCredit.domain.model.affiliate.Status; // Importa el enum Status del dominio.
import com.example.CoopCredit.domain.port.out.AffiliateRepositoryPort; // Importa el puerto de salida AffiliateRepositoryPort.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.affiliate.dto.AffiliateRequest; // Importa el DTO de solicitud de afiliado.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.affiliate.dto.AffiliateResponse; // Importa el DTO de respuesta de afiliado.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.http.HttpStatus; // Importa HttpStatus para códigos de estado HTTP.
import org.springframework.stereotype.Service; // Importa @Service para marcar la clase como un servicio.
import org.springframework.transaction.annotation.Transactional; // Importa @Transactional para gestión de transacciones.
import org.springframework.web.server.ResponseStatusException; // Importa ResponseStatusException para manejar errores HTTP.

import java.util.Date; // Importa Date.
import java.util.Optional; // Importa Optional.

@Service // Marca esta clase como un servicio de Spring.
public class AffiliateService { // Clase de servicio para la lógica de negocio de afiliados.

    private final AffiliateRepositoryPort affiliateRepositoryPort; // Inyecta el puerto de repositorio de afiliados.

    @Autowired // Constructor para inyección de dependencias.
    public AffiliateService(AffiliateRepositoryPort affiliateRepositoryPort) {
        this.affiliateRepositoryPort = affiliateRepositoryPort; // Asigna el repositorio de afiliados.
    }

    @Transactional // Asegura que la operación sea transaccional.
    public AffiliateResponse registerAffiliate(AffiliateRequest request) {
        // Validar si el documento ya existe.
        if (affiliateRepositoryPort.existsByDocument(request.getDocument())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Affiliate with document " + request.getDocument() + " already exists.");
        }

        // Crear el modelo de dominio Affiliate.
        Affiliate affiliate = new Affiliate(
                request.getDocument(),
                request.getName(),
                request.getSalary(),
                request.getAffiliationDate(),
                request.getStatus()
        );

        // Validaciones del modelo de dominio Affiliate (ej. salario > 0).
        affiliate.validateSalary(request.getSalary());

        // Guardar el afiliado a través del puerto.
        Affiliate savedAffiliate = affiliateRepositoryPort.save(affiliate);

        // Convertir el modelo de dominio a DTO de respuesta.
        return new AffiliateResponse(
                savedAffiliate.getDocument(),
                savedAffiliate.getName(),
                savedAffiliate.getSalary(),
                savedAffiliate.getAffiliationDate(),
                savedAffiliate.getStatus()
        );
    }

    @Transactional(readOnly = true) // Asegura que la operación sea transaccional y de solo lectura.
    public Optional<AffiliateResponse> getAffiliateByDocument(Long document) {
        return affiliateRepositoryPort.findByDocument(document)
                .map(affiliate -> new AffiliateResponse(
                        affiliate.getDocument(),
                        affiliate.getName(),
                        affiliate.getSalary(),
                        affiliate.getAffiliationDate(),
                        affiliate.getStatus()
                ));
    }

    @Transactional // Asegura que la operación sea transaccional.
    public AffiliateResponse updateAffiliate(Long document, AffiliateRequest request) {
        Affiliate existingAffiliate = affiliateRepositoryPort.findByDocument(document)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Affiliate not found with document: " + document));

        // Actualizar los campos del modelo de dominio.
        existingAffiliate.setName(request.getName());
        existingAffiliate.setSalary(request.getSalary());
        existingAffiliate.setAffiliationDate(request.getAffiliationDate());
        existingAffiliate.setStatus(request.getStatus());

        // Validaciones del modelo de dominio Affiliate (ej. salario > 0).
        existingAffiliate.validateSalary(request.getSalary());

        // Guardar el afiliado actualizado.
        Affiliate updatedAffiliate = affiliateRepositoryPort.save(existingAffiliate);

        return new AffiliateResponse(
                updatedAffiliate.getDocument(),
                updatedAffiliate.getName(),
                updatedAffiliate.getSalary(),
                updatedAffiliate.getAffiliationDate(),
                updatedAffiliate.getStatus()
        );
    }
}
