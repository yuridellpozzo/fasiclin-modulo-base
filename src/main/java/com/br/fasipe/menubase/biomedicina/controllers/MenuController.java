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
        
        switch (sistemaUpper) {
            case "ODONTOLOGIA":
                if (checkPermissao(tipoProfi, "2")) menu.add(new MenuItemDTO("CAD. PRESCRIÇÃO DO PACIENTE", "#"));
                if (checkPermissao(tipoProfi, "2")) menu.add(new MenuItemDTO("PRESCRIÇÃO DE MEDICAMENTOS", "#"));
                if (checkPermissao(tipoProfi, "2", "3")) menu.add(new MenuItemDTO("ACOMP. EVOLUÇÃO PACIENTE", "#"));
                if (checkPermissao(tipoProfi, "3")) menu.add(new MenuItemDTO("HOMOLOGAÇÃO ODONTO", "#"));
                if (checkPermissao(tipoProfi, "4")) menu.add(new MenuItemDTO("CAD. TRATAMENTO", "#"));
                break;

            case "BIOMEDICINA":
                if (checkPermissao(tipoProfi, "3")) menu.add(new MenuItemDTO("IMPRESSÃO DE LAUDOS", "#"));
                if (checkPermissao(tipoProfi, "3")) menu.add(new MenuItemDTO("EMISSÃO DE LAUDO", "#"));
                if (checkPermissao(tipoProfi, "2")) menu.add(new MenuItemDTO("RELATÓRIO DE PRODUÇÃO", "#"));
                if (checkPermissao(tipoProfi, "2")) menu.add(new MenuItemDTO("CADASTRO DE RESULTADOS", "#"));
                if (checkPermissao(tipoProfi, "2", "3")) menu.add(new MenuItemDTO("CAD. PEDIDOS E PRESCRIÇÃO", "#"));
                if (checkPermissao(tipoProfi, "2", "3")) menu.add(new MenuItemDTO("COLETA DE ASSINATURAS", "#"));
                break;

            default:
                if (!tipoProfi.equals("4")) { // Master pode ver menu vazio se sistema for novo
                   menu.add(new MenuItemDTO("Módulo não configurado: " + sistemaUpper, "#"));
                }
        }

        // Botão de Configurações para o Master
        if (tipoProfi.equals("4")) {
            menu.add(new MenuItemDTO("--- ADMINISTRAÇÃO ---", "#"));
            menu.add(new MenuItemDTO("CONFIGURAÇÕES DO MÓDULO", "/pages/configuracoes.html"));
        }

        return ResponseEntity.ok(menu);
    }

    private boolean checkPermissao(String usuarioTipo, String... tiposPermitidos) {
        // --- CORREÇÃO DO MASTER ---
        // Se for Master (4), tem permissão TOTAL em todos os botões do módulo dele
        if (usuarioTipo.equals("4")) {
            return true;
        }
        // --------------------------

        for (String tipo : tiposPermitidos) {
            if (usuarioTipo.equals(tipo)) {
                return true;
            }
        }
        return false;
    }
}