package com.example.CoopCredit.domain.port.out; // Define el paquete para los puertos de salida del dominio.

import com.example.CoopCredit.domain.model.affiliate.Credit; // Importa el modelo de dominio Credit.

import java.util.List; // Importa List para colecciones de créditos.
import java.util.Optional; // Importa Optional para manejar la posible ausencia de un resultado.

public interface CreditRepositoryPort { // Define la interfaz del puerto para la gestión de créditos.

    Credit save(Credit credit); // Método para guardar una solicitud de crédito.
    Optional<Credit> findById(Long id); // Método para buscar una solicitud de crédito por su ID.
    List<Credit> findAll(); // Método para obtener todas las solicitudes de crédito.
    List<Credit> findByAffiliateDocument(Long affiliateDocument); // CORRECCIÓN: Método para buscar solicitudes por documento de afiliado (Long).
    // Podríamos añadir métodos para buscar por estado, etc.
}
