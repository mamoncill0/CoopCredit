package com.example.CoopCredit.infrastructure.adapter.port.in.rest.affiliate.dto; // Define el paquete para los DTOs de afiliado.

import com.example.CoopCredit.domain.model.affiliate.Status; // Importa el enum Status del dominio.
import jakarta.validation.constraints.Min; // Importa la anotación para validar un valor mínimo.
import jakarta.validation.constraints.NotBlank; // Importa la anotación para validar que el campo no esté en blanco.
import jakarta.validation.constraints.NotNull; // Importa la anotación para validar que el campo no sea nulo.
import jakarta.validation.constraints.PastOrPresent; // Importa la anotación para validar que la fecha sea pasada o presente.
import jakarta.validation.constraints.Size; // Importa la anotación para validar el tamaño de la cadena.

import java.util.Date; // Importa Date para manejar fechas.

public class AffiliateRequest { // Clase que representa la solicitud de creación o actualización de un afiliado.

    @NotNull(message = "Document cannot be null") // Valida que el documento no sea nulo.
    @Min(value = 1, message = "Document must be a positive number") // Valida que el documento sea un número positivo.
    private Long document; // Documento de identificación del afiliado.

    @NotBlank(message = "Name cannot be blank") // Valida que el nombre no esté en blanco.
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters") // Valida la longitud del nombre.
    private String name; // Nombre completo del afiliado.

    @Min(value = 1, message = "Salary must be greater than 0") // Valida que el salario sea mayor que 0.
    private int salary; // Salario del afiliado.

    @NotNull(message = "Affiliation date cannot be null") // Valida que la fecha de afiliación no sea nula.
    @PastOrPresent(message = "Affiliation date cannot be in the future") // Valida que la fecha de afiliación no sea futura.
    private Date affiliationDate; // Fecha de afiliación.

    @NotNull(message = "Status cannot be null") // Valida que el estado no sea nulo.
    private Status status; // Estado del afiliado (ACTIVO/INACTIVO).

    // Getters y Setters para los campos.

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
