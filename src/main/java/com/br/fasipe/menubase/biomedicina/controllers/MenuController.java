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
        // 1. MENU ADMINISTRATIVO (CORINGA) - APENAS TIPO 1 (Admin) e TIPO 4 (Master)
        // =====================================================================
        if (sistemaUpper.equals("CORINGA")) {
            // Se for Tipo 1 (Admin) ou 4 (Master acessando Coringa)
            if (checkPermissao(tipoProfi, "1", "4")) {
                
                // --- MÓDULOS DE GESTÃO ---
                menu.add(new MenuItemDTO("COMPRAS", "#"));
                menu.add(new MenuItemDTO("ESTOQUE", "#"));
                menu.add(new MenuItemDTO("CONTAS A PAGAR", "#"));
                menu.add(new MenuItemDTO("VENDAS", "#"));
                menu.add(new MenuItemDTO("CONTAS A RECEBER", "#"));
                
                menu.add(new MenuItemDTO("--- OPERACIONAL ---", "#"));
                
                // --- MÓDULOS OPERACIONAIS (Que saíram da saúde) ---
                menu.add(new MenuItemDTO("CAD. DADOS PACIENTE", "#"));
                menu.add(new MenuItemDTO("REGISTRO DE DOCS. / MÍDIA", "#"));
            }
            
            return ResponseEntity.ok(menu);
        }

        // =====================================================================
        // 2. ITENS PADRÃO (Profissional Básico - Tipo 2) - Módulos de Saúde
        // =====================================================================
        boolean isProfissionalBasico = checkPermissao(tipoProfi, "2");
        
        if (isProfissionalBasico) {
            menu.add(new MenuItemDTO("REGISTRO DE DOCS/MÍDIA", "#"));
            menu.add(new MenuItemDTO("CAD. ANAMNESE", "#"));
            menu.add(new MenuItemDTO("REGISTRO DE PRONTUÁRIO", "#"));
            menu.add(new MenuItemDTO("--- ESPECÍFICO ---", "#")); 
        }

        // =====================================================================
        // 3. MENU ESPECÍFICO POR ESPECIALIDADE
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
                
            case "NUTRICAO":
                if (checkPermissao(tipoProfi, "2")) menu.add(new MenuItemDTO("AVALIAÇÃO NUTRICIONAL", "#"));
                if (checkPermissao(tipoProfi, "2", "3")) menu.add(new MenuItemDTO("ACOMP. EVOLUÇÃO PACIENTE", "#"));
                if (checkPermissao(tipoProfi, "3")) menu.add(new MenuItemDTO("HOMOLOGAÇÃO NUTRI", "#"));
                break;

            case "PSICOLOGIA":
                if (checkPermissao(tipoProfi, "2", "3")) menu.add(new MenuItemDTO("ACOMP. EVOLUÇÃO PACIENTE", "#"));
                if (checkPermissao(tipoProfi, "3")) menu.add(new MenuItemDTO("HOMOLOGAÇÃO PSICO", "#"));
                break;
                
            case "FISIOTERAPIA":
                if (checkPermissao(tipoProfi, "2", "3")) menu.add(new MenuItemDTO("ACOMP. EVOLUÇÃO PACIENTE", "#"));
                if (checkPermissao(tipoProfi, "3")) menu.add(new MenuItemDTO("HOMOLOGAÇÃO FISIO", "#"));
                break;

            default:
                if (tipoProfi.equals("4")) { 
                   menu.add(new MenuItemDTO("Módulo não configurado: " + sistemaUpper, "#"));
                }
        }

        // =====================================================================
        // 4. RODAPÉ DO MASTER (TIPOPROFI = 4)
        // =====================================================================
        if (tipoProfi.equals("4")) {
            menu.add(new MenuItemDTO("CAD. TRATAMENTO", "#")); 
            menu.add(new MenuItemDTO("--- ADMINISTRAÇÃO ---", "#"));
            menu.add(new MenuItemDTO("CONFIGURAÇÕES DO MÓDULO", "/pages/configuracoes.html"));
        }

        return ResponseEntity.ok(menu);
    }

    private boolean checkPermissao(String usuarioTipo, String... tiposPermitidos) {
        if (usuarioTipo.equals("4")) return true; // Master vê tudo
        for (String tipo : tiposPermitidos) {
            if (usuarioTipo.equals(tipo)) return true;
        }
        return false;
    }
}