package com.example.CoopCredit.infrastructure.config; // Define el paquete para la configuración de infraestructura.

import com.example.CoopCredit.domain.model.user.Role; // Importa el enum Role del dominio.
import com.example.CoopCredit.domain.port.out.RoleRepositoryPort; // Importa el puerto de salida RoleRepositoryPort.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.RoleEntity; // Importa la entidad RoleEntity de persistencia.
import org.springframework.boot.CommandLineRunner; // Importa CommandLineRunner para ejecutar código al inicio.
import org.springframework.stereotype.Component; // Importa @Component para que Spring detecte esta clase como un bean.

@Component // Marca esta clase como un componente de Spring.
public class RoleInitializer implements CommandLineRunner { // Implementa CommandLineRunner para ejecutar código al inicio de la aplicación.

    private final RoleRepositoryPort roleRepositoryPort; // Inyecta el puerto de repositorio de roles.

    // Constructor para inyección de dependencias.
    public RoleInitializer(RoleRepositoryPort roleRepositoryPort) {
        this.roleRepositoryPort = roleRepositoryPort; // Asigna el repositorio de roles.
    }

    @Override // Sobrescribe el método run, que se ejecuta al inicio de la aplicación.
    public void run(String... args) throws Exception {
        // Itera sobre todos los valores del enum Role.
        for (Role roleName : Role.values()) {
            // Busca el rol por su nombre en la base de datos.
            if (roleRepositoryPort.findByName(roleName).isEmpty()) {
                // Si el rol no existe, crea una nueva entidad RoleEntity y la guarda.
                roleRepositoryPort.save(new RoleEntity(roleName));
                System.out.println("Initialized role: " + roleName); // Imprime un mensaje de inicialización.
            }
        }
    }
}
