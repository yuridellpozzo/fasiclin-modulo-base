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
@CrossOrigin(origins = "http://localhost:5500")
public class MenuController {

    @GetMapping("/menu")
    public ResponseEntity<List<MenuItemDTO>> getMenu(
            @RequestParam String sistema,
            @RequestParam String tipoProfi
            ) {
        
        List<MenuItemDTO> menu = new ArrayList<>();
        String sistemaUpper = sistema.toUpperCase();
        
        // =====================================================================
        // ITENS PADRÃO (Profissional Básico - Tipo 2)
        // =====================================================================
        boolean isProfissionalBasico = checkPermissao(tipoProfi, "2");
        
        if (isProfissionalBasico && !sistemaUpper.equals("CORINGA")) {
            menu.add(new MenuItemDTO("REGISTRO DE DOCS/MÍDIA", "#"));
            menu.add(new MenuItemDTO("CAD. ANAMNESE", "#"));
            menu.add(new MenuItemDTO("REGISTRO DE PRONTUÁRIO", "#"));
            menu.add(new MenuItemDTO("--- ESPECÍFICO ---", "#")); 
        }

        // =====================================================================
        // MENU ESPECÍFICO POR SISTEMA
        // =====================================================================
        switch (sistemaUpper) {
            
            case "ODONTOLOGIA":
                if (checkPermissao(tipoProfi, "2")) {
                    menu.add(new MenuItemDTO("CAD. PRESCRIÇÃO DO PACIENTE", "#"));
                    menu.add(new MenuItemDTO("PRESCRIÇÃO DE MEDICAMENTOS", "#"));
                }
                if (checkPermissao(tipoProfi, "2", "3")) {
                    menu.add(new MenuItemDTO("ACOMP. EVOLUÇÃO PACIENTE", "#"));
                }
                if (checkPermissao(tipoProfi, "3")) {
                    menu.add(new MenuItemDTO("HOMOLOGAÇÃO ODONTO", "#"));
                }
                // Nota: CAD. TRATAMENTO movido para o bloco global do Master abaixo
                break;

            case "BIOMEDICINA":
                if (checkPermissao(tipoProfi, "3")) {
                    menu.add(new MenuItemDTO("IMPRESSÃO DE LAUDOS", "#"));
                    menu.add(new MenuItemDTO("EMISSÃO DE LAUDO", "#"));
                }
                if (checkPermissao(tipoProfi, "2")) {
                    menu.add(new MenuItemDTO("RELATÓRIO DE PRODUÇÃO", "#"));
                    menu.add(new MenuItemDTO("CADASTRO DE RESULTADOS", "#"));
                }
                if (checkPermissao(tipoProfi, "2", "3")) {
                    menu.add(new MenuItemDTO("CAD. PEDIDOS E PRESCRIÇÃO", "#"));
                    menu.add(new MenuItemDTO("COLETA DE ASSINATURAS", "#"));
                }
                break;
            
            case "CORINGA":
                 if (checkPermissao(tipoProfi, "1", "4")) {
                    menu.add(new MenuItemDTO("CAD. DADOS PACIENTE", "#"));
                    menu.add(new MenuItemDTO("REGISTRO DE DOCS/MÍDIA", "#"));
                 }
                 break;

            default:
                if (tipoProfi.equals("4")) { 
                   menu.add(new MenuItemDTO("Módulo não configurado: " + sistemaUpper, "#"));
                }
        }

        // =====================================================================
        // ÁREA EXCLUSIVA DO MASTER (TIPO 4)
        // =====================================================================
        if (tipoProfi.equals("4")) {
            
            // 1. Funcionalidade de Gestão do Master (Antes das configurações)
            menu.add(new MenuItemDTO("CAD. TRATAMENTO", "#")); // <-- ADICIONADO AQUI

            // 2. Configurações do Sistema
            menu.add(new MenuItemDTO("--- ADMINISTRAÇÃO ---", "#"));
            menu.add(new MenuItemDTO("CONFIGURAÇÕES DO MÓDULO", "/pages/configuracoes.html"));
        }

        return ResponseEntity.ok(menu);
    }

    // Método auxiliar para verificar permissão
    private boolean checkPermissao(String usuarioTipo, String... tiposPermitidos) {
        
        // O MASTER (4) tem acesso a TUDO
        if (usuarioTipo.equals("4")) {
            return true;
        }
        
        for (String tipo : tiposPermitidos) {
            if (usuarioTipo.equals(tipo)) {
                return true;
            }
        }
        return false;
    }
}