# CoopCredit - Sistema de Gestión de Créditos

Este proyecto implementa un sistema de gestión de créditos utilizando Spring Boot, siguiendo los principios de la Arquitectura Hexagonal (Puertos y Adaptadores). Incluye gestión de usuarios, autenticación y autorización con JWT, gestión de afiliados y un módulo de solicitud de créditos con reglas de negocio y evaluación de riesgo simulada.

## 🚀 Características Implementadas

*   **Arquitectura Hexagonal (Puertos y Adaptadores)**: Separación clara entre la lógica de negocio (dominio) y los detalles de infraestructura.
*   **Gestión de Usuarios**:
    *   Registro de usuarios (`/auth/register`).
    *   Inicio de sesión (`/auth/login`) con generación de JWT.
*   **Seguridad con JWT**:
    *   Autenticación basada en JSON Web Tokens (JWT) para un API REST sin estado.
    *   Encriptación de contraseñas con `BCryptPasswordEncoder`.
    *   Roles de usuario: `ROLE_MEMBER`, `ROLE_ANALYST`, `ROLE_ADMIN`.
*   **Control de Acceso Basado en Roles (`@PreAuthorize`)**:
    *   `MEMBER`: Solo puede solicitar créditos y ver sus propias solicitudes/perfil de afiliado.
    *   `ANALYST`: Puede ver solicitudes de crédito en estado `PENDING` y aprobar/rechazar créditos.
    *   `ADMIN`: Acceso completo a todas las funcionalidades (registro/actualización de afiliados, visualización de todos los créditos, aprobación/rechazo).
*   **Gestión de Afiliados**:
    *   Registro de afiliados (`POST /affiliates`).
    *   Consulta de afiliados por documento (`GET /affiliates/{document}`).
    *   Actualización de información de afiliados (`PUT /affiliates/{document}`).
*   **Solicitud de Créditos**:
    *   Registro de solicitudes de crédito (`POST /credits`).
    *   Evaluación de riesgo simulada (`/risk-evaluation`) mediante un servicio mock.
    *   Reglas de negocio para la aprobación/rechazo de créditos (ej. antigüedad mínima, monto máximo según salario, relación cuota/ingreso).
    *   Consulta de créditos por ID (`GET /credits/{id}`).
    *   Consulta de todos los créditos (`GET /credits`).
    *   Consulta de créditos pendientes (`GET /credits/pending`).
    *   Aprobación/Rechazo de créditos (`PUT /credits/{id}/approve`, `PUT /credits/{id}/reject`).
*   **Validaciones Avanzadas**:
    *   Uso de Bean Validation (`@NotNull`, `@Min`, `@Size`, etc.) en DTOs.
    *   Validaciones cruzadas en la lógica de negocio (ej. antigüedad del afiliado, monto/salario).
*   **Manejo Global de Errores**:
    *   `@ControllerAdvice` para capturar y formatear excepciones.
    *   Respuestas de error estandarizadas siguiendo el formato `ProblemDetail` (RFC 7807).
*   **Documentación de API**: Integración con Swagger UI (Springdoc-openapi).
*   **Pruebas Robustas**:
    *   **Unitarias**: Para la lógica de negocio pura (ej. `AuthService`).
    *   **Integración**: Para controladores y servicios, utilizando `MockMvc` y `Testcontainers` para una base de datos MySQL en un contenedor Docker.

## 🛠️ Tecnologías Utilizadas

*   **Spring Boot**: 3.2.0
*   **Java**: 17
*   **Maven**: Gestión de dependencias
*   **Spring Security**: Autenticación y autorización
*   **Spring Data JPA**: Persistencia de datos
*   **MySQL**: Base de datos relacional
*   **JJWT**: Generación y validación de JSON Web Tokens
*   **Springdoc-openapi (Swagger UI)**: Documentación interactiva de la API
*   **JUnit 5**: Framework de pruebas
*   **Mockito**: Creación de mocks para pruebas unitarias
*   **Testcontainers**: Contenedores Docker para pruebas de integración
*   **Lombok**: Reducción de código boilerplate

## ⚙️ Configuración y Ejecución

### Prerrequisitos

*   **Java 17** o superior
*   **Maven**
*   **Docker Desktop** (para Testcontainers en pruebas)
*   **MySQL Server** (para ejecución en desarrollo/producción)

### 1. Configuración de la Base de Datos

Asegúrate de tener un servidor MySQL corriendo. Crea una base de datos llamada `CoopCredit`.

Edita el archivo `src/main/resources/application.properties` con tus credenciales de MySQL:

```properties
# ConfiguracionParaAccederAMysql
spring.datasource.url=jdbc:mysql://localhost:3306/CoopCredit?useSSL=false&serverTimezone=UTC
spring.datasource.username=tu_usuario_mysql
spring.datasource.password=tu_contraseña_mysql

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update # Hibernate creará/actualizará las tablas automáticamente
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
```

### 2. Generación de la Clave Secreta JWT

La aplicación requiere una clave secreta JWT segura. Utiliza la siguiente clase `KeyGenerator` para generarla:

```java
// Puedes crear este archivo temporalmente en src/main/java/com/example/CoopCredit/KeyGenerator.java
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Generated JWT Secret Key (Base64 encoded): " + base64Key);
    }
}
```

Ejecuta el método `main` de `KeyGenerator`, copia la clave generada (será una cadena larga de caracteres Base64) y pégala en `application.properties`:

```properties
# Configuración de JWT
application.security.jwt.secret-key=TU_CLAVE_BASE64_GENERADA_AQUI
application.security.jwt.expiration=86400000 # 24 horas en milisegundos
```

### 3. Ejecutar la Aplicación

Desde la raíz del proyecto, puedes ejecutar la aplicación usando Maven:

```bash
mvn spring-boot:run
```

O desde tu IDE (IntelliJ IDEA), ejecuta la clase `CoopCreditApplication.java`.

La aplicación estará disponible en `http://localhost:8080`.

## 🌐 API Endpoints

La documentación interactiva de la API está disponible a través de Swagger UI:

*   **Swagger UI**: `http://localhost:8080/swagger-ui.html`
*   **Definición OpenAPI**: `http://localhost:8080/v3/api-docs`

### Flujo de Prueba Recomendado (Postman/Swagger UI)

Sigue este orden para probar las funcionalidades y el control de acceso:

1.  **`POST /auth/register`**:
    *   Registra un usuario `ADMIN` (ej. `username: adminUser`, `password: AdminPassword123`, `role: ["ADMIN"]`).
    *   Registra un usuario `MEMBER` (ej. `username: 103`, `password: MemberPassword123`, `role: ["MEMBER"]`). El `username` será el documento del afiliado.
    *   Registra un usuario `ANALYST` (ej. `username: analystUser`, `password: AnalystPassword123`, `role: ["ANALYST"]`).

2.  **`POST /auth/login`**:
    *   Inicia sesión como `adminUser` para obtener el `adminToken`.
    *   Inicia sesión como `103` (el `MEMBER`) para obtener el `memberToken`.
    *   Inicia sesión como `analystUser` para obtener el `analystToken`.
    *   **¡IMPORTANTE!** Copia los `accessToken` de cada login. Los necesitarás para las peticiones protegidas.

3.  **`POST /affiliates`**:
    *   **Headers**: `Authorization: Bearer <adminToken>`
    *   **Body**:
        ```json
        {
          "document": 103,
          "name": "Michael Eladicto",
          "salary": 5000000,
          "affiliationDate": "2023-01-01T00:00:00.000+00:00",
          "status": "Active"
        }
        ```
    *   **Nota**: Ajusta `salary` y `affiliationDate` para pasar las validaciones de crédito.

4.  **`POST /credits`**:
    *   **Headers**: `Authorization: Bearer <memberToken>`
    *   **Body**:
        ```json
        {
          "affiliateDocument": 103,
          "amount": 500000,
          "term": 36,
          "proposedRate": 0.05
        }
        ```
    *   **Nota**: Ajusta `amount` y `term` para pasar las validaciones de crédito (monto máximo, relación cuota/ingreso) según el `salary` del afiliado.

5.  **`GET /credits/{id}`**:
    *   **Headers**: `Authorization: Bearer <adminToken>` (para cualquier crédito) o `<memberToken>` (para su propio crédito).
    *   **Endpoint**: `http://localhost:8080/credits/<ID_DEL_CREDITO>`

6.  **`GET /credits`**:
    *   **Headers**: `Authorization: Bearer <adminToken>`

7.  **`GET /credits/pending`**:
    *   **Headers**: `Authorization: Bearer <analystToken>` o `<adminToken>`

8.  **`PUT /credits/{id}/approve`**:
    *   **Headers**: `Authorization: Bearer <analystToken>` o `<adminToken>`
    *   **Endpoint**: `http://localhost:8080/credits/<ID_DEL_CREDITO>/approve`
    *   **Body (opcional)**: `"Approved after review."`

9.  **`PUT /credits/{id}/reject`**:
    *   **Headers**: `Authorization: Bearer <analystToken>` o `<adminToken>`
    *   **Endpoint**: `http://localhost:8080/credits/<ID_DEL_CREDITO>/reject`
    *   **Body (opcional)**: `"Rejected due to high risk."`

## ✅ Pruebas

Para ejecutar las pruebas unitarias y de integración, asegúrate de tener Docker Desktop corriendo.

Desde la raíz del proyecto:

```bash
mvn clean install
```

Esto ejecutará:
*   **Pruebas Unitarias**: Para `AuthService`.
*   **Pruebas de Integración**: Para `AuthController`, `AffiliateController`, y `GlobalExceptionHandler`, utilizando Testcontainers para levantar una base de datos MySQL en un contenedor Docker.

## 🚧 Notas Adicionales

*   Las pruebas de integración de `CreditService` y `CreditController` no fueron implementadas debido a restricciones de tiempo, pero seguirían un patrón similar a las pruebas de `AffiliateController`.
*   El `RiskCentralService` es un mock interno y no un servicio REST externo real.
