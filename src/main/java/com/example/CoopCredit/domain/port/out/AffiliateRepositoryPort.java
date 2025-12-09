package com.example.CoopCredit.domain.port.out; // Define el paquete para los puertos de salida del dominio.

import com.example.CoopCredit.domain.model.affiliate.Affiliate; // Importa el modelo de dominio Affiliate.

import java.util.Optional; // Importa Optional para manejar la posible ausencia de un resultado.

public interface AffiliateRepositoryPort { // Define la interfaz del puerto para la gestión de afiliados.

    Optional<Affiliate> findByDocument(Long document); // Método para buscar un afiliado por su documento.
    Affiliate save(Affiliate affiliate); // Método para guardar un afiliado.
    boolean existsByDocument(Long document); // Método para verificar si un afiliado con un documento específico ya existe.
}
