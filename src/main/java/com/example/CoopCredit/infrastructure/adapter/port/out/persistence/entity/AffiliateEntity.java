package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity; // Define el paquete para las entidades de persistencia.

import com.example.CoopCredit.domain.model.affiliate.Status; // Importa el enum Status del dominio.
import jakarta.persistence.*; // Importa todas las anotaciones de JPA.

import java.util.Date; // Importa Date para manejar fechas.

@Entity // Marca esta clase como una entidad JPA.
@Table(name = "affiliates") // Especifica el nombre de la tabla en la base de datos.
public class AffiliateEntity { // Define la entidad de afiliado para la capa de persistencia.

    @Id // Marca este campo como la clave primaria.
    private Long document; // Documento del afiliado (usado como ID).

    @Column(nullable = false, length = 100) // Mapea a una columna no nula con longitud máxima.
    private String name; // Nombre del afiliado.

    @Column(nullable = false) // Mapea a una columna no nula.
    private int salary; // Salario del afiliado.

    @Temporal(TemporalType.DATE) // Especifica que la fecha debe persistirse sin información de tiempo.
    @Column(name = "affiliation_date", nullable = false) // Mapea a una columna no nula con nombre específico.
    private Date affiliationDate; // Fecha de afiliación.

    @Enumerated(EnumType.STRING) // Especifica que el enum 'Status' debe persistirse como una cadena de texto.
    @Column(nullable = false, length = 20) // Mapea a una columna no nula con longitud máxima.
    private Status status; // Estado del afiliado (Activo/Inactivo).

    // Constructor por defecto requerido por JPA.
    public AffiliateEntity() {
    }

    // Constructor para crear una instancia de AffiliateEntity con todos los datos.
    public AffiliateEntity(Long document, String name, int salary, Date affiliationDate, Status status) {
        this.document = document; // Inicializa el documento.
        this.name = name; // Inicializa el nombre.
        this.salary = salary; // Inicializa el salario.
        this.affiliationDate = affiliationDate; // Inicializa la fecha de afiliación.
        this.status = status; // Inicializa el estado.
    }

    // Getters y Setters para los campos de la entidad.

    public Long getDocument() { // Método para obtener el documento.
        return document; // Retorna el documento.
    }

    public void setDocument(Long document) { // Método para establecer el documento.
        this.document = document; // Asigna el documento.
    }

    public String getName() { // Método para obtener el nombre.
        return name; // Retorna el nombre.
    }

    public void setName(String name) { // Método para establecer el nombre.
        this.name = name; // Asigna el nombre.
    }

    public int getSalary() { // Método para obtener el salario.
        return salary; // Retorna el salario.
    }

    public void setSalary(int salary) { // Método para establecer el salario.
        this.salary = salary; // Asigna el salario.
    }

    public Date getAffiliationDate() { // Método para obtener la fecha de afiliación.
        return affiliationDate; // Retorna la fecha de afiliación.
    }

    public void setAffiliationDate(Date affiliationDate) { // Método para establecer la fecha de afiliación.
        this.affiliationDate = affiliationDate; // Asigna la fecha de afiliación.
    }

    public Status getStatus() { // Método para obtener el estado.
        return status; // Retorna el estado.
    }

    public void setStatus(Status status) { // Método para establecer el estado.
        this.status = status; // Asigna el estado.
    }
}
