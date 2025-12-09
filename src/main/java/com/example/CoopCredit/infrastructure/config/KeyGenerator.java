package com.example.CoopCredit.infrastructure.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;

// Aqui es donde creamos las keys que se ponen en el properties
public class KeyGenerator {
    public static void main(String[] args) {
        // Genera una clave segura para HS512 (que requiere al menos 256 bits)
        byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
        // Codifica la clave en Base64 para poder guardarla en application.properties
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Generated JWT Secret Key (Base64 encoded): " + base64Key);
    }
}
