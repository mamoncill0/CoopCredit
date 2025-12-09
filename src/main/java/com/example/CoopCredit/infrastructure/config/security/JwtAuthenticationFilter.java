package com.example.CoopCredit.infrastructure.config.security; // Define el paquete para los componentes de seguridad en la capa de infraestructura.

import jakarta.servlet.FilterChain; // Importa FilterChain para encadenar filtros.
import jakarta.servlet.ServletException; // Importa ServletException para manejar errores de servlet.
import jakarta.servlet.http.HttpServletRequest; // Importa HttpServletRequest para manejar peticiones HTTP.
import jakarta.servlet.http.HttpServletResponse; // Importa HttpServletResponse para manejar respuestas HTTP.
import org.slf4j.Logger; // Importa Logger para registrar mensajes.
import org.slf4j.LoggerFactory; // Importa LoggerFactory para obtener una instancia de Logger.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Importa la clase para el token de autenticación.
import org.springframework.security.core.context.SecurityContextHolder; // Importa SecurityContextHolder para gestionar el contexto de seguridad.
import org.springframework.security.core.userdetails.UserDetails; // Importa UserDetails para representar los detalles del usuario.
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Importa WebAuthenticationDetailsSource para detalles de autenticación web.
import org.springframework.stereotype.Component; // Importa @Component para que Spring detecte esta clase como un bean.
import org.springframework.util.AntPathMatcher; // Importa AntPathMatcher para comparar patrones de URL.
import org.springframework.util.StringUtils; // Importa StringUtils para utilidades de cadenas.
import org.springframework.web.filter.OncePerRequestFilter; // Importa OncePerRequestFilter para asegurar que el filtro se ejecute una vez por petición.

import java.io.IOException; // Importa IOException para manejar errores de entrada/salida.
import java.util.Arrays; // Importa Arrays para manejar arreglos.
import java.util.List; // Importa List para manejar listas.

@Component // Marca esta clase como un componente de Spring.
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Filtro para autenticación JWT, se ejecuta una vez por petición.

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class); // Logger para registrar eventos y errores.

    @Autowired // Inyecta el JwtProvider para manejar tokens JWT.
    private JwtProvider jwtProvider;

    @Autowired // Inyecta el UserDetailsServiceImpl para cargar detalles del usuario.
    private UserDetailsServiceImpl userDetailsService;

    // Lista de rutas que deben ser excluidas del filtro JWT.
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/auth/**", // Rutas de autenticación
            "/risk-evaluation/**", // Rutas del microservicio de evaluación de riesgo
            // "/affiliates/**", // ELIMINADO: Las rutas de afiliados ahora requieren autenticación JWT
            "/v3/api-docs/**", // Rutas de OpenAPI
            "/swagger-ui/**", // Rutas de Swagger UI
            "/swagger-ui.html" // Página principal de Swagger UI
    );

    // AntPathMatcher para comparar patrones de URL.
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override // Sobrescribe el método doFilterInternal para implementar la lógica del filtro.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException { // Lógica principal del filtro.
        try {
            String jwt = parseJwt(request); // Extrae el token JWT de la petición.
            if (jwt != null && jwtProvider.validateJwtToken(jwt)) { // Si el token existe y es válido.
                String username = jwtProvider.getUserNameFromJwtToken(jwt); // Obtiene el nombre de usuario del token.

                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Carga los detalles del usuario.
                UsernamePasswordAuthenticationToken authentication = // Crea un objeto de autenticación.
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Establece detalles de autenticación.

                SecurityContextHolder.getContext().setAuthentication(authentication); // Establece la autenticación en el contexto de seguridad.
            }
        } catch (Exception e) { // Captura cualquier excepción durante el proceso de autenticación.
            logger.error("Cannot set user authentication: {}", e.getMessage()); // Registra el error.
        }

        filterChain.doFilter(request, response); // Continúa la cadena de filtros.
    }

    // Método privado para extraer el token JWT del encabezado de la petición.
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization"); // Obtiene el encabezado "Authorization".

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) { // Si el encabezado no está vacío y comienza con "Bearer ".
            return headerAuth.substring(7); // Retorna el token JWT (después de "Bearer ").
        }

        return null; // Retorna null si no se encuentra un token válido.
    }

    @Override // Sobrescribe el método shouldNotFilter para especificar qué rutas deben ser excluidas del filtro.
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Comprueba si la ruta de la petición coincide con alguna de las rutas excluidas.
        return EXCLUDE_URLS.stream().anyMatch(pattern -> pathMatcher.match(pattern, request.getServletPath()));
    }
}
