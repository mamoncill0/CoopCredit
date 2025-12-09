package com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity; // Define el paquete para las entidades de persistencia.

import jakarta.persistence.*; // Importa todas las anotaciones de JPA.
import org.springframework.security.core.GrantedAuthority; // Importa GrantedAuthority para los roles de seguridad.
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Importa SimpleGrantedAuthority para implementar GrantedAuthority.
import org.springframework.security.core.userdetails.UserDetails; // Importa UserDetails para la integración con Spring Security.

import java.util.Collection; // Importa Collection para la colección de autoridades.
import java.util.HashSet; // Importa HashSet para la colección de roles.
import java.util.Set; // Importa Set para la colección de roles.
import java.util.stream.Collectors; // Importa Collectors para operaciones de stream.

@Entity // Marca esta clase como una entidad JPA.
@Table(name = "users", // Especifica el nombre de la tabla en la base de datos.
        uniqueConstraints = { // Define restricciones de unicidad para columnas.
                @UniqueConstraint(columnNames = "username"), // Asegura que el nombre de usuario sea único.
                @UniqueConstraint(columnNames = "email") // Asegura que el correo electrónico sea único.
        })
public class UserEntity implements UserDetails { // Define la entidad de usuario que implementa UserDetails para Spring Security.

    @Id // Marca este campo como la clave primaria.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la estrategia de generación de ID (autoincremental).
    private Long id; // Identificador único del usuario.

    @Column(nullable = false, length = 50) // Mapea a una columna no nula con longitud máxima.
    private String username; // Nombre de usuario para el login.

    @Column(nullable = false, length = 50) // Mapea a una columna no nula con longitud máxima.
    private String email; // Correo electrónico del usuario.

    @Column(nullable = false, length = 120) // Mapea a una columna no nula con longitud máxima.
    private String password; // Contraseña del usuario (encriptada).

    @ManyToMany(fetch = FetchType.EAGER) // Define una relación Many-to-Many con RoleEntity, cargando roles de forma ansiosa.
    @JoinTable(name = "user_roles", // Especifica la tabla de unión para la relación.
            joinColumns = @JoinColumn(name = "user_id"), // Columna de unión para UserEntity.
            inverseJoinColumns = @JoinColumn(name = "role_id")) // Columna de unión para RoleEntity.
    private Set<RoleEntity> roles = new HashSet<>(); // Conjunto de roles asociados a este usuario.

    // Constructor por defecto requerido por JPA.
    public UserEntity() {
    }

    // Constructor para crear una instancia de UserEntity con datos básicos.
    public UserEntity(String username, String email, String password) {
        this.username = username; // Inicializa el nombre de usuario.
        this.email = email; // Inicializa el correo electrónico.
        this.password = password; // Inicializa la contraseña.
    }

    // Getters y Setters para los campos de la entidad.

    public Long getId() { // Método para obtener el ID.
        return id; // Retorna el ID.
    }

    public void setId(Long id) { // Método para establecer el ID.
        this.id = id; // Asigna el ID.
    }

    @Override // Sobrescribe el método getUsername de UserDetails.
    public String getUsername() { // Método para obtener el nombre de usuario.
        return username; // Retorna el nombre de usuario.
    }

    public void setUsername(String username) { // Método para establecer el nombre de usuario.
        this.username = username; // Asigna el nombre de usuario.
    }

    public String getEmail() { // Método para obtener el correo electrónico.
        return email; // Retorna el correo electrónico.
    }

    public void setEmail(String email) { // Método para establecer el correo electrónico.
        this.email = email; // Asigna el correo electrónico.
    }

    @Override // Sobrescribe el método getPassword de UserDetails.
    public String getPassword() { // Método para obtener la contraseña.
        return password; // Retorna la contraseña.
    }

    public void setPassword(String password) { // Método para establecer la contraseña.
        this.password = password; // Asigna la contraseña.
    }

    public Set<RoleEntity> getRoles() { // Método para obtener el conjunto de roles.
        return roles; // Retorna los roles.
    }

    public void setRoles(Set<RoleEntity> roles) { // Método para establecer el conjunto de roles.
        this.roles = roles; // Asigna los roles.
    }

    // Implementación de los métodos de la interfaz UserDetails.

    @Override // Sobrescribe el método getAuthorities para obtener los roles como GrantedAuthority.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream() // Convierte el conjunto de RoleEntity a un stream.
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name())) // CORRECCIÓN: Añade el prefijo "ROLE_" al nombre del rol.
                .collect(Collectors.toList()); // Recolecta las autoridades en una lista.
    }

    @Override // Indica si la cuenta del usuario no ha expirado.
    public boolean isAccountNonExpired() {
        return true; // Por simplicidad, siempre retorna true.
    }

    @Override // Indica si la cuenta del usuario no está bloqueada.
    public boolean isAccountNonLocked() {
        return true; // Por simplicidad, siempre retorna true.
    }

    @Override // Indica si las credenciales del usuario no han expirado.
    public boolean isCredentialsNonExpired() {
        return true; // Por simplicidad, siempre retorna true.
    }

    @Override // Indica si el usuario está habilitado.
    public boolean isEnabled() {
        return true; // Por simplicidad, siempre retorna true.
    }
}
