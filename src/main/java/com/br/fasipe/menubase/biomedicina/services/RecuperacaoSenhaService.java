package com.br.fasipe.menubase.biomedicina.services;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RecuperacaoSenhaService {

    // Guarda: Código -> Login do Usuário
    private final Map<String, String> tokenStorage = new HashMap<>();
    
    // Guarda: Código -> Hora que expira
    private final Map<String, LocalDateTime> expiryStorage = new HashMap<>();

    public String gerarCodigo(String login) {
        // Gera código de 6 números para ficar fácil digitar
        // (Ajustei para números pois UUID gera letras que podem confundir)
        String codigo = String.valueOf(100000 + (int)(Math.random() * 900000));
        
        tokenStorage.put(codigo, login);
        expiryStorage.put(codigo, LocalDateTime.now().plusMinutes(15)); // 15 min de validade
        
        return codigo;
    }

    public String validarCodigo(String codigo) {
        if (!tokenStorage.containsKey(codigo)) return null; // Não existe
        
        if (LocalDateTime.now().isAfter(expiryStorage.get(codigo))) {
            tokenStorage.remove(codigo); // Remove expirado
            expiryStorage.remove(codigo);
            return "EXPIRED";
        }
        
        return tokenStorage.get(codigo); // Retorna o Login dono do código
    }

    public void queimarCodigo(String codigo) {
        tokenStorage.remove(codigo);
        expiryStorage.remove(codigo);
    }
}