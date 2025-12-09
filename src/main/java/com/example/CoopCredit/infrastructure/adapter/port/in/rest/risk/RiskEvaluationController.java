package com.example.CoopCredit.infrastructure.adapter.port.in.rest.risk; // Paquete para controladores en la capa de infraestructura.

import com.example.CoopCredit.application.service.RiskEvaluationService; // Importa el servicio de la capa de aplicación.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.risk.dto.RiskEvaluationRequest; // Importa el DTO de la solicitud.
import com.example.CoopCredit.infrastructure.adapter.port.in.rest.risk.dto.RiskEvaluationResponse; // Importa el DTO de la respuesta.
import org.springframework.beans.factory.annotation.Autowired; // Importa la anotación para inyección de dependencias.
import org.springframework.http.ResponseEntity; // Importa la clase para encapsular la respuesta HTTP.
import org.springframework.web.bind.annotation.PostMapping; // Importa la anotación para mapear peticiones POST.
import org.springframework.web.bind.annotation.RequestBody; // Importa la anotación para vincular el cuerpo de la petición.
import org.springframework.web.bind.annotation.RequestMapping; // Importa la anotación para la ruta base del controlador.
import org.springframework.web.bind.annotation.RestController; // Importa la anotación para controladores REST.

@RestController // Marca esta clase como un controlador REST.
@RequestMapping("/risk-evaluation") // Mapea las peticiones a la ruta /risk-evaluation.
public class RiskEvaluationController { // Define la clase del controlador.

    private final RiskEvaluationService riskEvaluationService; // Declara una dependencia final al servicio de aplicación.

    @Autowired // Inyecta la dependencia del servicio a través del constructor.
    public RiskEvaluationController(RiskEvaluationService riskEvaluationService) { // Constructor para la inyección de dependencias.
        this.riskEvaluationService = riskEvaluationService; // Asigna el servicio inyectado al campo de la clase.
    }

    @PostMapping // Mapea las peticiones HTTP POST a este mét0do.
    public ResponseEntity<RiskEvaluationResponse> evaluateRisk(@RequestBody RiskEvaluationRequest request) { // Define el mét0do del endpoint.
        // @RequestBody convierte el cuerpo JSON de la petición en un objeto RiskEvaluationRequest.
        
        RiskEvaluationResponse response = riskEvaluationService.evaluate(request); // Delega el procesamiento de la solicitud al servicio de aplicación.
        
        return ResponseEntity.ok(response); // Retorna la respuesta en un ResponseEntity con estado HTTP 200 OK.
    }
}
