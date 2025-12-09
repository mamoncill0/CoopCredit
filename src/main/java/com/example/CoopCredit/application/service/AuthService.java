package com.example.CoopCredit.application.service; // Define el paquete para los servicios de la aplicación.

import com.example.CoopCredit.domain.model.user.Role; // Importa el enum Role del dominio.
import com.example.CoopCredit.domain.model.user.User; // Importa el modelo de dominio User.
import com.example.CoopCredit.domain.port.out.RoleRepositoryPort; // Importa el puerto de salida RoleRepositoryPort.
import com.example.CoopCredit.domain.port.out.UserRepositoryPort; // Importa el puerto de salida UserRepositoryPort.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.dto.AuthResponse; // Importa el DTO de respuesta de autenticación.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.dto.LoginRequest; // Importa el DTO de solicitud de login.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.user.dto.RegisterRequest; // Importa el DTO de solicitud de registro.
import com.example.CoopCredit.infrastructure.adapter.port.out.persistence.entity.UserEntity; // Importa UserEntity para la autenticación de Spring Security.
import com.example.CoopCredit.infrastructure.config.security.JwtProvider; // Importa el proveedor de JWT.
import org.springframework.beans.factory.annotation.Autowired; // Importa @Autowired para inyección de dependencias.
import org.springframework.http.HttpStatus; // Importa HttpStatus para códigos de estado HTTP.
import org.springframework.security.authentication.AuthenticationManager; // Importa AuthenticationManager para gestionar la autenticación.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Importa UsernamePasswordAuthenticationToken.
import org.springframework.security.core.Authentication; // Importa Authentication de Spring Security.
import org.springframework.security.core.GrantedAuthority; // Importa GrantedAuthority para los roles.
import org.springframework.security.core.context.SecurityContextHolder; // Importa SecurityContextHolder para gestionar el contexto de seguridad.
import org.springframework.security.crypto.password.PasswordEncoder; // Importa PasswordEncoder para encriptar contraseñas.
import org.springframework.stereotype.Service; // Importa @Service para marcar la clase como un servicio.
import org.springframework.web.server.ResponseStatusException; // Importa ResponseStatusException para manejar errores HTTP.

import java.util.HashSet; // Importa HashSet para colecciones de roles.
import java.util.Set; // Importa Set para colecciones de roles.
import java.util.stream.Collectors; // Importa Collectors para operaciones de stream.

@Service // Marca esta clase como un servicio de Spring, parte de la capa de aplicación.
public class AuthService { // Clase de servicio para la lógica de autenticación y registro.

    private final AuthenticationManager authenticationManager; // Inyecta el AuthenticationManager para la autenticación.
    private final UserRepositoryPort userRepositoryPort; // Inyecta el puerto de repositorio de usuarios del dominio.
    private final RoleRepositoryPort roleRepositoryPort; // Inyecta el puerto de repositorio de roles del dominio.
    private final PasswordEncoder encoder; // Inyecta el PasswordEncoder para encriptar contraseñas.
    private final JwtProvider jwtProvider; // Inyecta el JwtProvider para generar tokens.

    @Autowired // Constructor para inyección de dependencias.
    public AuthService(AuthenticationManager authenticationManager,
                       UserRepositoryPort userRepositoryPort,
                       RoleRepositoryPort roleRepositoryPort,
                       PasswordEncoder encoder,
                       JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager; // Asigna el AuthenticationManager.
        this.userRepositoryPort = userRepositoryPort; // Asigna el UserRepositoryPort.
        this.roleRepositoryPort = roleRepositoryPort; // Asigna el RoleRepositoryPort.
        this.encoder = encoder; // Asigna el PasswordEncoder.
        this.jwtProvider = jwtProvider; // Asigna el JwtProvider.
    }

    // Método para autenticar un usuario y generar un token JWT.
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate( // Autentica al usuario usando el nombre de usuario y contraseña.
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication); // Establece la autenticación en el contexto de seguridad.
        String jwt = jwtProvider.generateJwtToken(authentication); // Genera un token JWT para el usuario autenticado.

        UserEntity userDetails = (UserEntity) authentication.getPrincipal(); // Obtiene los detalles del usuario (UserEntity) del principal autenticado.
        Set<String> roles = userDetails.getAuthorities().stream() // Obtiene los roles del usuario como cadenas.
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // Retorna un AuthResponse con el token y la información básica del usuario.
        return new AuthResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles.iterator().next()); // Simplificado para retornar solo el primer rol.
    }

    // Método para registrar un nuevo usuario.
    public void registerUser(RegisterRequest signUpRequest) {
        if (userRepositoryPort.existsByUsername(signUpRequest.getUsername())) { // Verifica si el nombre de usuario ya existe.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Username is already taken!");
        }

        if (userRepositoryPort.existsByEmail(signUpRequest.getEmail())) { // Verifica si el correo electrónico ya existe.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Email is already in use!");
        }

        // Crea un nuevo objeto User del dominio.
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())); // Encripta la contraseña antes de crear el objeto User.

        Set<String> strRoles = signUpRequest.getRole(); // Obtiene los roles de la solicitud.
        Set<Role> roles = new HashSet<>(); // Inicializa un conjunto para los roles del dominio.

        if (strRoles == null || strRoles.isEmpty()) { // Si no se especifican roles o la lista está vacía, asigna el rol por defecto (MEMBER).
            Role userRole = roleRepositoryPort.findByName(Role.MEMBER) // Busca el rol MEMBER a través del puerto.
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error: Role MEMBER is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(roleName -> { // Itera sobre los roles especificados.
                Role roleEnum;
                try {
                    // Convierte el nombre del rol a mayúsculas sin añadir el prefijo "ROLE_".
                    roleEnum = Role.valueOf(roleName.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Invalid role specified: " + roleName);
                }

                Role foundRole = roleRepositoryPort.findByName(roleEnum) // Busca el rol a través del puerto.
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error: Role " + roleEnum + " is not found."));
                roles.add(foundRole);
            });
        }

        user.setRoles(roles); // Asigna los roles al objeto User del dominio.
        userRepositoryPort.save(user); // Guarda el usuario a través del puerto de repositorio.
    }
}
