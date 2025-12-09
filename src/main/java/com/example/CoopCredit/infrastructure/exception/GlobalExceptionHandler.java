package com.example.CoopCredit.infrastructure.exception; // Define el paquete para el manejo global de excepciones.

import org.springframework.http.HttpStatus; // Importa HttpStatus para códigos de estado HTTP.
import org.springframework.http.ProblemDetail; // Importa ProblemDetail para el formato de error RFC 7807.
import org.springframework.http.ResponseEntity; // Importa ResponseEntity para encapsular respuestas HTTP.
import org.springframework.security.access.AccessDeniedException; // Importa AccessDeniedException para errores de autorización.
import org.springframework.web.bind.MethodArgumentNotValidException; // Importa MethodArgumentNotValidException para errores de validación de Bean Validation.
import org.springframework.web.bind.annotation.ControllerAdvice; // Importa @ControllerAdvice para el manejo global de excepciones.
import org.springframework.web.bind.annotation.ExceptionHandler; // Importa @ExceptionHandler para mapear excepciones a métodos.
import org.springframework.web.server.ResponseStatusException; // Importa ResponseStatusException.

import java.net.URI; // Importa URI para el campo 'type' de ProblemDetail.
import java.time.Instant; // Importa Instant para el campo 'timestamp'.
import java.util.UUID; // Importa UUID para generar un 'traceId'.
import java.util.stream.Collectors; // Importa Collectors para operaciones de stream.

@ControllerAdvice // Marca esta clase para manejar excepciones globalmente en todos los controladores.
public class GlobalExceptionHandler { // Clase para el manejo centralizado de excepciones.

    // Maneja excepciones de tipo ResponseStatusException (lanzadas por nuestros servicios).
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatusException(ResponseStatusException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason()); // Crea un ProblemDetail con el estado y detalle de la excepción.
        problemDetail.setType(URI.create("https://example.com/problems/business-rule-violation")); // Define un tipo URI para el problema.
        problemDetail.setTitle("Business Rule Violation"); // Define un título para el problema.
        problemDetail.setProperty("timestamp", Instant.now()); // Añade la marca de tiempo.
        problemDetail.setProperty("traceId", UUID.randomUUID().toString()); // Añade un ID de traza único.
        return ResponseEntity.status(ex.getStatusCode()).body(problemDetail); // Retorna la respuesta con el ProblemDetail.
    }

    // Maneja excepciones de validación de Bean Validation.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed for request parameters."); // Crea un ProblemDetail para errores de validación.
        problemDetail.setType(URI.create("https://example.com/problems/validation-error")); // Define un tipo URI.
        problemDetail.setTitle("Invalid Request Data"); // Define un título.
        problemDetail.setProperty("timestamp", Instant.now()); // Añade la marca de tiempo.
        problemDetail.setProperty("traceId", UUID.randomUUID().toString()); // Añade un ID de traza.
        // Añade detalles específicos de los errores de campo.
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        problemDetail.setProperty("errors", errors); // Añade los errores de campo como una propiedad.
        return ResponseEntity.badRequest().body(problemDetail); // Retorna la respuesta con estado 400 Bad Request.
    }

    // Maneja excepciones de acceso denegado (errores de autorización de Spring Security).
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage()); // Crea un ProblemDetail para acceso denegado.
        problemDetail.setType(URI.create("https://example.com/problems/access-denied")); // Define un tipo URI.
        problemDetail.setTitle("Access Denied"); // Define un título.
        problemDetail.setProperty("timestamp", Instant.now()); // Añade la marca de tiempo.
        problemDetail.setProperty("traceId", UUID.randomUUID().toString()); // Añade un ID de traza.
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problemDetail); // Retorna la respuesta con estado 403 Forbidden.
    }

    // Maneja cualquier otra excepción no capturada específicamente.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred."); // Crea un ProblemDetail para errores internos del servidor.
        problemDetail.setType(URI.create("https://example.com/problems/internal-server-error")); // Define un tipo URI.
        problemDetail.setTitle("Internal Server Error"); // Define un título.
        problemDetail.setProperty("timestamp", Instant.now()); // Añade la marca de tiempo.
        problemDetail.setProperty("traceId", UUID.randomUUID().toString()); // Añade un ID de traza.
        problemDetail.setProperty("debugMessage", ex.getMessage()); // Añade el mensaje de la excepción para depuración.
        return ResponseEntity.internalServerError().body(problemDetail); // Retorna la respuesta con estado 500 Internal Server Error.
    }
}
