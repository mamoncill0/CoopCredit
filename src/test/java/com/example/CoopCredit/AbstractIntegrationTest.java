package com.example.CoopCredit; // Define el paquete para la clase base de pruebas.

import org.springframework.boot.test.context.SpringBootTest; // Importa SpringBootTest.
import org.springframework.test.context.ActiveProfiles; // Importa ActiveProfiles.
import org.springframework.test.context.DynamicPropertyRegistry; // Importa DynamicPropertyRegistry.
import org.springframework.test.context.DynamicPropertySource; // Importa DynamicPropertySource.
import org.testcontainers.containers.MySQLContainer; // Importa MySQLContainer.
import org.testcontainers.junit.jupiter.Container; // Importa Container.
import org.testcontainers.junit.jupiter.Testcontainers; // Importa Testcontainers.
import org.testcontainers.utility.DockerImageName; // Importa DockerImageName.

@SpringBootTest // Carga el contexto completo de Spring Boot para la prueba.
@ActiveProfiles("test") // Activa el perfil de prueba para usar application-test.properties.
@Testcontainers // Habilita el soporte para Testcontainers en JUnit 5.
public abstract class AbstractIntegrationTest { // Clase base abstracta para pruebas de integración.

    // Define un contenedor MySQL que se iniciará una vez para todas las pruebas.
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("testdb") // Nombre de la base de datos.
            .withUsername("test") // Nombre de usuario.
            .withPassword("test"); // Contraseña.

    // Configura propiedades dinámicas de Spring para que apunten al contenedor MySQL.
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl); // URL de conexión a la base de datos.
        registry.add("spring.datasource.username", mysql::getUsername); // Nombre de usuario.
        registry.add("spring.datasource.password", mysql::getPassword); // Contraseña.
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop"); // Crea y elimina tablas en cada ejecución de prueba.
    }
}
