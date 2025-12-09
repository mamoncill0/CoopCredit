package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity; // Define el paquete para las entidades de persistencia en la capa de infraestructura.

import com.example.CoopCredit.domain.model.user.Role; // Importa el enum Role del dominio.

import jakarta.persistence.*; // Importa todas las anotaciones de JPA necesarias.

@Entity // Marca esta clase como una entidad JPA, lo que significa que se mapeará a una tabla de base de datos.
@Table(name = "roles") // Especifica el nombre de la tabla en la base de datos a la que se mapeará esta entidad.
public class RoleEntity { // Define la clase de entidad para los roles, específica para la capa de persistencia.

    @Id // Marca este campo como la clave primaria de la entidad.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la estrategia de generación de valores para la clave primaria (autoincremental).
    private Integer id; // Identificador único del rol.

    @Enumerated(EnumType.STRING) // Especifica que el enum 'Role' debe persistirse como una cadena de texto en la base de datos.
    @Column(length = 20, unique = true, nullable = false) // Mapea este campo a una columna, establece su longitud máxima, asegura que sea único y no nulo.
    private Role name; // El nombre del rol, utilizando el enum Role del dominio.

    // Constructor por defecto requerido por JPA.
    public RoleEntity() {
    }

    // Constructor para crear una instancia de RoleEntity con un nombre de rol específico.
    public RoleEntity(Role name) {
        this.name = name; // Inicializa el nombre del rol.
    }

    // Getters y Setters para acceder y modificar los campos de la entidad.

    public Integer getId() { // Método para obtener el ID del rol.
        return id; // Retorna el ID.
    }

    public void setId(Integer id) { // Método para establecer el ID del rol.
        this.id = id; // Asigna el ID.
    }

    public Role getName() { // Método para obtener el nombre del rol.
        return name; // Retorna el nombre del rol (enum).
    }

    public void setName(Role name) { // Método para establecer el nombre del rol.
        this.name = name; // Asigna el nombre del rol.
    }
}
