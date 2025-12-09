package com.br.fasipe.menubase.biomedicina.services;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenService {

    // Guarda: Código -> Login do Usuário
    private final Map<String, String> tokenStorage = new HashMap<>();
    
    // Guarda: Código -> Hora que expira
    private final Map<String, LocalDateTime> expiryStorage = new HashMap<>();

    public String gerarToken(String login) {
        // Gera código de 6 letras/números maiúsculos
        String token = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        
        tokenStorage.put(token, login);
        expiryStorage.put(token, LocalDateTime.now().plusMinutes(15)); // 15 min de validade
        
        return token;
    }

    public String validarToken(String token) {
        if (!tokenStorage.containsKey(token)) return null; // Não existe
        
        if (LocalDateTime.now().isAfter(expiryStorage.get(token))) {
            tokenStorage.remove(token); // Remove expirado
            expiryStorage.remove(token);
            return "EXPIRED";
        }
        
        return tokenStorage.get(token); // Retorna o Login dono do token
    }

    public void queimarToken(String token) {
        tokenStorage.remove(token);
        expiryStorage.remove(token);
    }
}