package com.example.CoopCredit.domain.model.affiliate;

import java.util.Date;
import java.util.List;

public class Affiliate {
    private Long document;
    private String name;
    private int salary;
    private Date affiliationDate;
    private Status status;

    public Affiliate() {
    }

    public Affiliate(Long document, String name, int salary, Date affiliationDate, Status status) {
        this.document = document;
        this.name = name;
        this.salary = salary;
        this.affiliationDate = affiliationDate;
        this.status = status;
    }

    //Reglas de negocio

    public boolean canRequestAppointment() {
        return this.status == Status.Active;
    }

    public void deactivate() {
        if (this.status == Status.Inactive) {
            throw new IllegalStateException("El afiliado ahora está inactivo y no puede solicitar un credito");
        }
        this.status = Status.Inactive;
    }

    public void activate() {
        if (this.status == Status.Active) {
            throw new IllegalStateException("El afiliado ahora está activa y puede solicitar un credito");
        }
        this.status = Status.Active;
    }

    // Validaciones

    public void validateSalary(int salary){
        if (salary <= 0){
            throw new IllegalArgumentException("El salario debe ser mayor a 0");
        }
    }

    public void valiteDocument(List<Long> existingDocuments){
        if (existingDocuments.contains(this.document)){
            throw new IllegalArgumentException("El documento ya existe");
        }
    }


    public Long getDocument() {
        return document;
    }

    public void setDocument(Long document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Date getAffiliationDate() {
        return affiliationDate;
    }

    public void setAffiliationDate(Date affiliationDate) {
        this.affiliationDate = affiliationDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return  "document=" + document +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", affiliationDate=" + affiliationDate +
                ", status=" + status +
                '}';
    }
}
