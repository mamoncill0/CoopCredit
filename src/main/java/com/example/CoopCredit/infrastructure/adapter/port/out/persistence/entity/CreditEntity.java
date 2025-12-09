package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity; // Define el paquete para las entidades de persistencia.

import com.example.CoopCredit.domain.model.affiliate.StatusCredit; // Importa el enum StatusCredit del dominio.
import jakarta.persistence.*; // Importa todas las anotaciones de JPA.

import java.math.BigDecimal; // Importa BigDecimal para manejar valores monetarios.
import java.time.LocalDate; // Importa LocalDate para manejar fechas.

@Entity // Marca esta clase como una entidad JPA.
@Table(name = "credits") // Especifica el nombre de la tabla en la base de datos.
public class CreditEntity { // Define la entidad de crédito para la capa de persistencia.

    @Id // Marca este campo como la clave primaria.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la estrategia de generación de ID.
    private Long id; // Identificador único de la solicitud de crédito.

    @ManyToOne(fetch = FetchType.LAZY) // Define una relación Many-to-One con AffiliateEntity, cargando de forma perezosa.
    @JoinColumn(name = "affiliate_document", nullable = false) // Especifica la columna de unión y que no puede ser nula.
    private AffiliateEntity affiliate; // Afiliado solicitante.

    @Column(nullable = false, precision = 19, scale = 2) // Mapea a una columna no nula con precisión y escala para BigDecimal.
    private BigDecimal amount; // Monto solicitado.

    @Column(nullable = false) // Mapea a una columna no nula.
    private int term; // Plazo en meses.

    @Column(name = "proposed_rate", nullable = false, precision = 5, scale = 4) // Mapea a una columna no nula con nombre, precisión y escala.
    private BigDecimal proposedRate; // Tasa propuesta.

    @Column(name = "request_date", nullable = false) // Mapea a una columna no nula con nombre específico.
    private LocalDate requestDate; // Fecha de solicitud.

    @Enumerated(EnumType.STRING) // Especifica que el enum 'StatusCredit' debe persistirse como una cadena de texto.
    @Column(name = "status_credit", nullable = false, length = 20) // Mapea a una columna no nula con nombre y longitud específicos.
    private StatusCredit statusCredit; // Estado de la solicitud de crédito.

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Define una relación One-to-One con RiskEvaluationEntity, con cascada ALL y carga perezosa.
    @JoinColumn(name = "risk_evaluation_id") // Especifica la columna de unión.
    private RiskEvaluationEntity associatedEvaluation; // Evaluación de riesgo asociada.

    // Constructor por defecto requerido por JPA.
    public CreditEntity() {
    }

    // Constructor para crear una instancia de CreditEntity con datos básicos.
    public CreditEntity(AffiliateEntity affiliate, BigDecimal amount, int term, BigDecimal proposedRate, LocalDate requestDate, StatusCredit statusCredit) {
        this.affiliate = affiliate;
        this.amount = amount;
        this.term = term;
        this.proposedRate = proposedRate;
        this.requestDate = requestDate;
        this.statusCredit = statusCredit;
    }

    // Getters y Setters para los campos de la entidad.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AffiliateEntity getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(AffiliateEntity affiliate) {
        this.affiliate = affiliate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public BigDecimal getProposedRate() {
        return proposedRate;
    }

    public void setProposedRate(BigDecimal proposedRate) {
        this.proposedRate = proposedRate;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public StatusCredit getStatusCredit() {
        return statusCredit;
    }

    public void setStatusCredit(StatusCredit statusCredit) {
        this.statusCredit = statusCredit;
    }

    public RiskEvaluationEntity getAssociatedEvaluation() {
        return associatedEvaluation;
    }

    public void setAssociatedEvaluation(RiskEvaluationEntity associatedEvaluation) {
        this.associatedEvaluation = associatedEvaluation;
    }
}
