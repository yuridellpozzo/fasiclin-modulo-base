package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.dto.MenuItemDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5500") // Permite o Live Server
public class MenuController {

    @GetMapping("/menu")
    public ResponseEntity<List<MenuItemDTO>> getMenu(
            @RequestParam String sistema, 
            @RequestParam String cargo) {
        
        List<MenuItemDTO> menu = new ArrayList<>();

        // Lógica para carregar o menu baseado no sistema
        switch (sistema.toUpperCase()) {
            case "BIOMEDICINA":
                menu.add(new MenuItemDTO("ANAMNESE", "#"));
                menu.add(new MenuItemDTO("CAD. DADOS PACIENTE", "#"));
                menu.add(new MenuItemDTO("CAD. PEDIDOS E PRESCRIÇÃO", "#"));
                menu.add(new MenuItemDTO("CAD. RESULTADOS", "#"));
                menu.add(new MenuItemDTO("COLETA DE ASSINATURAS", "#"));
                menu.add(new MenuItemDTO("EMISSÃO DE LAUDO", "#"));
                menu.add(new MenuItemDTO("IMPRESSÃO DE LAUDOS", "#"));
                menu.add(new MenuItemDTO("REGISTRO DE PRONTUÁRIO", "#"));
                menu.add(new MenuItemDTO("REGISTROS", "#"));
                menu.add(new MenuItemDTO("RELATÓRIO DE PRODUÇÃO", "#"));
                break;
            
            case "ODONTOLOGIA":
                menu.add(new MenuItemDTO("CAD. DADOS PACIENTE", "#"));
                menu.add(new MenuItemDTO("CONFIGURAÇÕES", "#"));
                menu.add(new MenuItemDTO("PRESCRIÇÃO DE MEDICAMENTOS", "#"));
                menu.add(new MenuItemDTO("CAD. PRESCRIÇÃO DO PACIENTE", "#"));
                menu.add(new MenuItemDTO("HOMOLOGAÇÃO ODONTO", "#"));
                menu.add(new MenuItemDTO("CAD. TRATAMENTO", "#"));
                break;

            case "CORINGA": // Módulos Coringa que você listou
                menu.add(new MenuItemDTO("CAD. ANAMNESE", "#"));
                menu.add(new MenuItemDTO("REGISTRO DE DOCS. / MÍDIA", "#"));
                menu.add(new MenuItemDTO("REGISTRO DE PRONTUÁRIO", "#"));
                menu.add(new MenuItemDTO("CAD. DADOS PACIENTE", "#"));
                break;
            
            // Adicione 'case' para "NUTRICAO", "PSICOLOGIA", etc.
            
            default:
                menu.add(new MenuItemDTO("Módulo não encontrado", "#"));
        }

        // Lógica de permissão baseada no Cargo
        // Se for Supervisor ou Admin, adiciona botões especiais
        if (cargo.equals("Supervisor") || cargo.equals("Administrador")) {
            menu.add(new MenuItemDTO("--- GESTÃO ---", "#")); // Um divisor
            menu.add(new MenuItemDTO("RELATÓRIOS DO MÓDULO", "#"));
            menu.add(new MenuItemDTO("PAINEL DO SUPERVISOR", "#"));
        }

        return ResponseEntity.ok(menu);
    }
}