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
            @RequestParam String sistema,  // Ex: "ODONTOLOGIA"
            @RequestParam String tipoProfi // Ex: "2", "3", "4"
            ) {
        
        List<MenuItemDTO> menu = new ArrayList<>();

        // Padroniza para evitar erros de maiúsculas/minúsculas
        String sistemaUpper = sistema.toUpperCase();
        
        // --- LÓGICA ESPECÍFICA POR SISTEMA ---
        switch (sistemaUpper) {
            
            // =================================================================
            // ODONTOLOGIA
            // =================================================================
            case "ODONTOLOGIA":
                // CAD. PRESCRIÇÃO DO PACIENTE (Tipo 2)
                if (checkPermissao(tipoProfi, "2")) {
                    menu.add(new MenuItemDTO("CAD. PRESCRIÇÃO DO PACIENTE", "#"));
                }
                // PRESCRIÇÃO DE MEDICAMENTOS (Tipo 2)
                if (checkPermissao(tipoProfi, "2")) {
                    menu.add(new MenuItemDTO("PRESCRIÇÃO DE MEDICAMENTOS", "#"));
                }
                // ACOMP. EVOLUÇÃO PACIENTE (Tipo 2 ou 3)
                if (checkPermissao(tipoProfi, "2", "3")) {
                    menu.add(new MenuItemDTO("ACOMP. EVOLUÇÃO PACIENTE", "#"));
                }
                // HOMOLOGAÇÃO ODONTO (Tipo 3)
                if (checkPermissao(tipoProfi, "3")) {
                    menu.add(new MenuItemDTO("HOMOLOGAÇÃO ODONTO", "#"));
                }
                // CAD. TRATAMENTO (Tipo 4 - Master)
                if (checkPermissao(tipoProfi, "4")) {
                    menu.add(new MenuItemDTO("CAD. TRATAMENTO", "#"));
                }
                break;

            // =================================================================
            // BIOMEDICINA
            // =================================================================
            case "BIOMEDICINA":
                // IMPRESSÃO DE LAUDOS (Tipo 3)
                if (checkPermissao(tipoProfi, "3")) {
                    menu.add(new MenuItemDTO("IMPRESSÃO DE LAUDOS", "#"));
                }
                // EMISSÃO DE LAUDO (Tipo 3)
                if (checkPermissao(tipoProfi, "3")) {
                    menu.add(new MenuItemDTO("EMISSÃO DE LAUDO", "#"));
                }
                // RELATÓRIO DE PRODUÇÃO (Tipo 2)
                if (checkPermissao(tipoProfi, "2")) {
                    menu.add(new MenuItemDTO("RELATÓRIO DE PRODUÇÃO", "#"));
                }
                // CADASTRO DE RESULTADOS (Tipo 2)
                if (checkPermissao(tipoProfi, "2")) {
                    menu.add(new MenuItemDTO("CADASTRO DE RESULTADOS", "#"));
                }
                // CAD. PEDIDOS E PRESCRIÇÃO (Tipo 2 ou 3)
                if (checkPermissao(tipoProfi, "2", "3")) {
                    menu.add(new MenuItemDTO("CAD. PEDIDOS E PRESCRIÇÃO", "#"));
                }
                // COLETA DE ASSINATURAS (Tipo 2 ou 3)
                if (checkPermissao(tipoProfi, "2", "3")) {
                    menu.add(new MenuItemDTO("COLETA DE ASSINATURAS", "#"));
                }
                break;

            // =================================================================
            // SISTEMA NÃO ENCONTRADO
            // =================================================================
            default:
                menu.add(new MenuItemDTO("Módulo não configurado: " + sistemaUpper, "#"));
        }

        // --- ITENS COMUNS PARA TODOS (EXCETO TIPO 1 QUE TEM TELA PRÓPRIA) ---
        // (Se desejar adicionar itens comuns como "Meus Dados", coloque aqui)


        // --- LÓGICA DO MASTER (TIPO 4) - CONFIGURAÇÕES ---
        // O Master sempre vê o botão de configurações no final
        if (tipoProfi.equals("4")) {
            menu.add(new MenuItemDTO("--- ADMINISTRAÇÃO ---", "#"));
            menu.add(new MenuItemDTO("CONFIGURAÇÕES DO MÓDULO", "/pages/configuracoes.html"));
        }

        return ResponseEntity.ok(menu);
    }

    // Método auxiliar para verificar se o tipoProfi está na lista de permitidos
    private boolean checkPermissao(String usuarioTipo, String... tiposPermitidos) {
        // O Master (4) geralmente vê tudo? Se sim, descomente a linha abaixo:
        // if (usuarioTipo.equals("4")) return true; 
        
        for (String tipo : tiposPermitidos) {
            if (usuarioTipo.equals(tipo)) {
                return true;
            }
        }
        return false;
    }
}