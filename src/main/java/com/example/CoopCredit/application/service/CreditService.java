package com.example.CoopCredit.application.service; // Define el paquete para los servicios de la aplicación.

import com.example.CoopCredit.domain.model.affiliate.Affiliate; // Importa el modelo de dominio Affiliate.
import com.example.CoopCredit.domain.model.affiliate.Credit; // Importa el modelo de dominio Credit.
import com.example.CoopCredit.domain.model.affiliate.RiskEvaluation; // Importa el modelo de dominio RiskEvaluation.
import com.example.CoopCredit.domain.model.affiliate.RiskLevel; // Importa el enum RiskLevel.
import com.example.CoopCredit.domain.model.affiliate.StatusCredit; // Importa el enum StatusCredit.
import com.example.CoopCredit.domain.port.out.AffiliateRepositoryPort; // Importa el puerto de salida AffiliateRepositoryPort.
import com.example.CoopCredit.domain.port.out.CreditRepositoryPort; // Importa el puerto de salida CreditRepositoryPort.
import com.example.CoopCredit.domain.port.out.RiskEvaluationRepositoryPort; // Importa el puerto de salida RiskEvaluationRepositoryPort.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.risk.dto.RiskEvaluationRequest; // Importa el DTO de solicitud de evaluación de riesgo.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.risk.dto.RiskEvaluationResponse; // Importa el DTO de respuesta de evaluación de riesgo.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.http.HttpStatus; // Importa HttpStatus para códigos de estado HTTP.
import org.springframework.stereotype.Service; // Importa @Service para marcar la clase como un servicio.
import org.springframework.transaction.annotation.Transactional; // Importa @Transactional para gestión de transacciones.
import org.springframework.web.server.ResponseStatusException; // Importa ResponseStatusException para manejar errores HTTP.

import java.math.BigDecimal; // Importa BigDecimal.
import java.math.RoundingMode; // Importa RoundingMode para operaciones con BigDecimal.
import java.time.LocalDate; // Importa LocalDate.
import java.time.Period; // Importa Period para calcular la diferencia entre fechas.
import java.time.ZoneId; // Importa ZoneId para convertir Date a LocalDate.
import java.util.List; // Importa List.
import java.util.Optional; // Importa Optional.
import java.util.stream.Collectors; // Importa Collectors.

@Service // Marca esta clase como un servicio de Spring.
public class CreditService { // Clase de servicio para la lógica de negocio de créditos.

    private final CreditRepositoryPort creditRepositoryPort; // Inyecta el puerto de repositorio de créditos.
    private final AffiliateRepositoryPort affiliateRepositoryPort; // Inyecta el puerto de repositorio de afiliados.
    private final RiskEvaluationRepositoryPort riskEvaluationRepositoryPort; // Inyecta el puerto de repositorio de evaluaciones de riesgo.
    private final RiskEvaluationService riskEvaluationService; // Inyecta el servicio de evaluación de riesgo.

    @Autowired // Constructor para inyección de dependencias.
    public CreditService(CreditRepositoryPort creditRepositoryPort,
                         AffiliateRepositoryPort affiliateRepositoryPort,
                         RiskEvaluationRepositoryPort riskEvaluationRepositoryPort,
                         RiskEvaluationService riskEvaluationService) {
        this.creditRepositoryPort = creditRepositoryPort;
        this.affiliateRepositoryPort = affiliateRepositoryPort;
        this.riskEvaluationRepositoryPort = riskEvaluationRepositoryPort;
        this.riskEvaluationService = riskEvaluationService;
    }

    @Transactional // Asegura que la operación sea transaccional.
    public Credit requestCredit(Long affiliateDocument, BigDecimal amount, int term, BigDecimal proposedRate) {
        // 1. Validar afiliado activo y antigüedad (reglas de negocio).
        Affiliate affiliate = affiliateRepositoryPort.findByDocument(affiliateDocument)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Affiliate not found with document: " + affiliateDocument));

        // Validaciones del modelo de dominio Affiliate
        if (!affiliate.canRequestAppointment()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Affiliate must be ACTIVE to request a credit.");
        }

        // Validación de antigüedad mínima (ej. 6 meses)
        if (affiliate.getAffiliationDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Affiliation date is missing for affiliate.");
        }
        LocalDate affiliationLocalDate = affiliate.getAffiliationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (Period.between(affiliationLocalDate, LocalDate.now()).toTotalMonths() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Affiliate must have at least 6 months of affiliation.");
        }

        // Validación de monto máximo según salario (ej. 5 veces el salario)
        BigDecimal maxAmount = BigDecimal.valueOf(affiliate.getSalary()).multiply(BigDecimal.valueOf(5));
        if (amount.compareTo(maxAmount) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested amount exceeds maximum allowed (5x salary). Max amount: " + maxAmount);
        }

        // Validación de relación cuota/ingreso (ej. cuota mensual no excede 30% del salario)
        // Simplificación: cuota mensual = monto / plazo
        if (term <= 0) { // Asegurar que el plazo sea válido para evitar división por cero.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit term must be greater than 0.");
        }
        BigDecimal monthlyPayment = amount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
        BigDecimal maxMonthlyPayment = BigDecimal.valueOf(affiliate.getSalary()).multiply(BigDecimal.valueOf(0.30)); // 30% del salario.

        if (monthlyPayment.compareTo(maxMonthlyPayment) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Monthly payment exceeds 30% of affiliate's salary. Max monthly payment: " + maxMonthlyPayment);
        }


        // 2. Crear la solicitud de crédito inicial (estado PENDING).
        Credit credit = new Credit(affiliate, amount, term, proposedRate);
        credit.setRequestDate(LocalDate.now()); // Asegurar la fecha de solicitud
        credit.setStatusCredit(StatusCredit.PENDING); // Asegurar estado inicial

        // 3. Invocar al risk-central-mock-service.
        RiskEvaluationRequest riskRequest = new RiskEvaluationRequest(); // Usar constructor por defecto.
        riskRequest.setDocument(affiliateDocument.toString()); // Usar setter.
        riskRequest.setAmount(amount); // Usar setter.
        riskRequest.setTerm(term); // Usar setter.

        RiskEvaluationResponse riskResponse = riskEvaluationService.evaluate(riskRequest);

        // 4. Generar EvaluaciónRiesgo y decidir APROBADO o RECHAZADO.
        RiskEvaluation domainRiskEvaluation = new RiskEvaluation(
                riskResponse.getScore(),
                RiskLevel.valueOf(riskResponse.getRiskLevel()), // CORRECCIÓN: Convertir String a RiskLevel enum.
                riskResponse.getDetail()
        );
        RiskEvaluation savedRiskEvaluation = riskEvaluationRepositoryPort.save(domainRiskEvaluation); // Guardar la evaluación de riesgo.

        credit.setAssociatedEvaluation(savedRiskEvaluation); // Asociar la evaluación al crédito.

        // Aplicar políticas internas (relación cuota/ingreso, monto máximo, etc.)
        // Por ahora, una lógica simple basada en el nivel de riesgo
        if (riskResponse.getRiskLevel().equals("HIGH")) {
            credit.reject();
            // Crear una nueva instancia de RiskEvaluation con la razón actualizada.
            RiskEvaluation updatedRiskEvaluation = new RiskEvaluation(
                    savedRiskEvaluation.getId(), // Usar el ID de la evaluación guardada.
                    savedRiskEvaluation.getScore(),
                    savedRiskEvaluation.getRiskLevel(),
                    "Rejected due to HIGH risk level and internal policies."
            );
            credit.setAssociatedEvaluation(riskEvaluationRepositoryPort.save(updatedRiskEvaluation));
        } else {
            credit.approve();
            // Crear una nueva instancia de RiskEvaluation con la razón actualizada.
            RiskEvaluation updatedRiskEvaluation = new RiskEvaluation(
                    savedRiskEvaluation.getId(), // Usar el ID de la evaluación guardada.
                    savedRiskEvaluation.getScore(),
                    savedRiskEvaluation.getRiskLevel(),
                    "Approved based on risk evaluation and internal policies."
            );
            credit.setAssociatedEvaluation(riskEvaluationRepositoryPort.save(updatedRiskEvaluation));
        }

        return creditRepositoryPort.save(credit); // Guardar y retornar la solicitud de crédito actualizada.
    }

    @Transactional(readOnly = true)
    public Optional<Credit> getCreditById(Long id) {
        return creditRepositoryPort.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Credit> getAllCredits() {
        return creditRepositoryPort.findAll();
    }

    @Transactional(readOnly = true)
    public List<Credit> getCreditsByAffiliateDocument(Long affiliateDocument) {
        return creditRepositoryPort.findByAffiliateDocument(affiliateDocument);
    }

    // Nuevo método para obtener solicitudes de crédito en estado PENDING.
    @Transactional(readOnly = true)
    public List<Credit> getPendingCredits() {
        return creditRepositoryPort.findAll().stream()
                .filter(credit -> credit.getStatusCredit() == StatusCredit.PENDING)
                .collect(Collectors.toList());
    }

    // Método para verificar si un crédito pertenece al afiliado de un usuario autenticado.
    @Transactional(readOnly = true)
    public boolean isCreditOwnedByAuthenticatedUser(Long creditId, String authenticatedUsername) {
        Optional<Credit> creditOptional = creditRepositoryPort.findById(creditId);
        if (creditOptional.isEmpty()) {
            return false; // El crédito no existe.
        }
        Credit credit = creditOptional.get();

        // Asumimos que el username del UserEntity es el mismo que el document del Affiliate.
        // Si esta relación es diferente, necesitaríamos un mapeo explícito o buscar el Affiliate por username.
        Long affiliateDocumentFromCredit = credit.getAffiliateDocument().getDocument();
        Long authenticatedUserDocument = Long.valueOf(authenticatedUsername); // Convertir username a Long para comparar.

        return affiliateDocumentFromCredit.equals(authenticatedUserDocument);
    }

    // Métodos para aprobar/rechazar (con control de acceso)
    @Transactional
    public Credit approveCredit(Long creditId, String decisionReason) {
        Credit credit = creditRepositoryPort.findById(creditId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit not found with ID: " + creditId));

        if (credit.getStatusCredit() != StatusCredit.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit can only be approved if it is in PENDING status.");
        }

        // Crear una nueva instancia de RiskEvaluation con la razón actualizada.
        RiskEvaluation riskEvaluation = new RiskEvaluation(
                credit.getAssociatedEvaluation().getId(), // Usar el ID de la evaluación existente.
                credit.getAssociatedEvaluation().getScore(),
                credit.getAssociatedEvaluation().getRiskLevel(),
                decisionReason != null ? decisionReason : "Approved by analyst/admin."
        );
        credit.approve();
        credit.setAssociatedEvaluation(riskEvaluationRepositoryPort.save(riskEvaluation)); // Guardar la evaluación y asociarla.
        return creditRepositoryPort.save(credit);
    }

    @Transactional
    public Credit rejectCredit(Long creditId, String decisionReason) {
        Credit credit = creditRepositoryPort.findById(creditId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit not found with ID: " + creditId));

        if (credit.getStatusCredit() != StatusCredit.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credit can only be rejected if it is in PENDING status.");
        }

        // Crear una nueva instancia de RiskEvaluation con la razón actualizada.
        RiskEvaluation riskEvaluation = new RiskEvaluation(
                credit.getAssociatedEvaluation().getId(), // Usar el ID de la evaluación existente.
                credit.getAssociatedEvaluation().getScore(),
                credit.getAssociatedEvaluation().getRiskLevel(),
                decisionReason != null ? decisionReason : "Rejected by analyst/admin."
        );
        credit.reject();
        credit.setAssociatedEvaluation(riskEvaluationRepositoryPort.save(riskEvaluation)); // Guardar la evaluación y asociarla.
        return creditRepositoryPort.save(credit);
    }
}
