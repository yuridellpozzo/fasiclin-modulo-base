package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.dto.ModuleThemeDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "http://localhost:5500")
public class ConfigController {

    // O arquivo ficará na raiz do projeto (onde está o pom.xml)
    private static final String FILE_PATH = "module-themes.json";
    private final ObjectMapper mapper = new ObjectMapper();

    // 1. CARREGAR CONFIGURAÇÃO
    @GetMapping("/{sistema}")
    public ResponseEntity<ModuleThemeDTO> getConfig(@PathVariable String sistema) {
        Map<String, ModuleThemeDTO> themes = loadThemes();
        
        // Se existir config para esse sistema, retorna. Se não, retorna vazio (usar padrão).
        if (themes.containsKey(sistema.toUpperCase())) {
            return ResponseEntity.ok(themes.get(sistema.toUpperCase()));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    // 2. SALVAR CONFIGURAÇÃO
    @PostMapping
    public ResponseEntity<String> saveConfig(@RequestBody ModuleThemeDTO config) {
        try {
            Map<String, ModuleThemeDTO> themes = loadThemes();
            
            // Atualiza ou cria a configuração para este sistema
            themes.put(config.getSistema().toUpperCase(), config);
            
            // Salva no arquivo
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), themes);
            
            return ResponseEntity.ok("Configuração salva com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao salvar: " + e.getMessage());
        }
    }

    // 3. RESETAR PARA PADRÃO
    @DeleteMapping("/{sistema}")
    public ResponseEntity<String> resetConfig(@PathVariable String sistema) {
        try {
            Map<String, ModuleThemeDTO> themes = loadThemes();
            
            // Remove a chave do sistema (assim ele volta a usar o CSS padrão)
            if (themes.containsKey(sistema.toUpperCase())) {
                themes.remove(sistema.toUpperCase());
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), themes);
                return ResponseEntity.ok("Configuração resetada para o padrão!");
            } else {
                return ResponseEntity.ok("Já está no padrão.");
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao resetar.");
        }
    }

    // Método auxiliar para ler o arquivo JSON
    private Map<String, ModuleThemeDTO> loadThemes() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try {
            return mapper.readValue(file, new TypeReference<Map<String, ModuleThemeDTO>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}