package com.example.CoopCredit.infrastructure.adapter.port.in.rest.affiliate.dto; // Define el paquete para los DTOs de afiliado.

import com.example.CoopCredit.domain.model.affiliate.Status; // Importa el enum Status del dominio.

import java.util.Date; // Importa Date para manejar fechas.

public class AffiliateResponse { // Clase que representa la respuesta de un afiliado.

    private Long document; // Documento de identificación del afiliado.
    private String name; // Nombre completo del afiliado.
    private int salary; // Salario del afiliado.
    private Date affiliationDate; // Fecha de afiliación.
    private Status status; // Estado del afiliado (ACTIVO/INACTIVO).

    // Constructor para crear una instancia de AffiliateResponse.
    public AffiliateResponse(Long document, String name, int salary, Date affiliationDate, Status status) {
        this.document = document; // Inicializa el documento.
        this.name = name; // Inicializa el nombre.
        this.salary = salary; // Inicializa el salario.
        this.affiliationDate = affiliationDate; // Inicializa la fecha de afiliación.
        this.status = status; // Inicializa el estado.
    }

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
