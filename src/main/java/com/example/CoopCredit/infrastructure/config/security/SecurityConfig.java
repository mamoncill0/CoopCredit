package com.example.CoopCredit.infrastructure.config.security; // Define el paquete para la configuración de seguridad.

import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.context.annotation.Bean; // Importa @Bean para declarar métodos que producen beans.
import org.springframework.context.annotation.Configuration; // Importa @Configuration para marcar la clase como fuente de definiciones de beans.
import org.springframework.security.authentication.AuthenticationManager; // Importa AuthenticationManager.
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Importa DaoAuthenticationProvider para la autenticación basada en DAO.
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Importa AuthenticationConfiguration.
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Habilita la seguridad a nivel de método.
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Importa HttpSecurity para configurar la seguridad web.
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Importa AbstractHttpConfigurer para deshabilitar CSRF.
import org.springframework.security.config.http.SessionCreationPolicy; // Importa SessionCreationPolicy para configurar la gestión de sesiones.
import org.springframework.security.core.userdetails.UserDetailsService; // Importa la interfaz UserDetailsService.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importa BCryptPasswordEncoder para la encriptación de contraseñas.
import org.springframework.security.crypto.password.PasswordEncoder; // Importa PasswordEncoder.
import org.springframework.security.web.SecurityFilterChain; // Importa SecurityFilterChain para configurar la cadena de filtros de seguridad.
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Importa UsernamePasswordAuthenticationFilter.

@Configuration // Marca esta clase como una clase de configuración de Spring.
@EnableMethodSecurity // Habilita la seguridad a nivel de método (ej. @PreAuthorize).
public class SecurityConfig { // Clase de configuración principal para Spring Security.

    @Autowired // Inyecta el servicio de detalles de usuario.
    UserDetailsService userDetailsService; // Inyecta la interfaz UserDetailsService.

    @Autowired // Inyecta el punto de entrada de autenticación JWT.
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired // Inyecta el filtro de autenticación JWT.
    private JwtAuthenticationFilter authenticationJwtTokenFilter;

    @Bean // Declara un bean para el codificador de contraseñas.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza BCrypt para encriptar contraseñas.
    }

    @Bean // Declara un bean para el proveedor de autenticación.
    public DaoAuthenticationProvider authenticationProvider() {
        // Instancia DaoAuthenticationProvider pasando userDetailsService en el constructor.
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder()); // Establece el codificador de contraseñas.
        return authProvider; // Retorna el proveedor configurado.
    }

    @Bean // Declara un bean para el gestor de autenticación.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager(); // Obtiene el gestor de autenticación de la configuración.
    }

    @Bean // Declara un bean para la cadena de filtros de seguridad.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Deshabilita la protección CSRF (común en APIs REST stateless).
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)) // Configura el manejador para excepciones de autenticación.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura la gestión de sesiones como STATELESS (sin estado).
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/auth/**").permitAll() // Permite el acceso a todos los endpoints bajo /auth.
                                .requestMatchers("/risk-evaluation/**").permitAll() // Permite el acceso a todos los endpoints bajo /risk-evaluation.
                                // Rutas de Swagger UI y OpenAPI para permitir el acceso sin autenticación
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                                .anyRequest().authenticated() // Requiere autenticación para cualquier otra petición.
                );

        http.authenticationProvider(authenticationProvider()); // Configura el proveedor de autenticación.

        http.addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class); // Añade el filtro JWT antes del filtro de usuario/contraseña.

        return http.build(); // Construye y retorna la cadena de filtros de seguridad.
    }
}
