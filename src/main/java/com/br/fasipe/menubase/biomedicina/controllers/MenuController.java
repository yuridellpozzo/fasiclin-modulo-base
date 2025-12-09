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
            @RequestParam String tipoProfi) {
        
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
                menu.add(new MenuItemDTO("CADASTRAR USUÁRIO", "/pages/cadastro-usuario.html"));
                menu.add(new MenuItemDTO("GESTÃO DE ADMINS", "/pages/gestao-equipe.html"));
            }
            
            return ResponseEntity.ok(menu);
        }

        // =====================================================================
        // 2. ITENS PADRÃO (Profissional Básico - Tipo 2) - Módulos de Saúde
        // =====================================================================
        boolean isProfissionalBasico = checkPermissao(tipoProfi, "2");
        
        if (isProfissionalBasico) {
            menu.add(new MenuItemDTO("CAD. ANAMNESE", "#"));
            menu.add(new MenuItemDTO("REGISTRO DE DOCS. / MÍDIA", "#"));
            menu.add(new MenuItemDTO("REGISTRO DE PRONTUÁRIO", "#"));
            menu.add(new MenuItemDTO("--- ESPECÍFICO ---", "#")); 
        }

        // =====================================================================
        // 3. MENU ESPECÍFICO POR ESPECIALIDADE
        // =====================================================================
        switch (sistemaUpper) {
            
            // --- ODONTOLOGIA ---
            case "ODONTOLOGIA":
                // Tipo 2 (Profissional) e 3 (Supervisor acompanha)
                if (checkPermissao(tipoProfi, "2", "3")) {
                    menu.add(new MenuItemDTO("CAD. PRESCRIÇÃO DO PACIENTE", "#"));
                    menu.add(new MenuItemDTO("PRESCRIÇÃO DE MEDICAMENTOS", "#"));
                    menu.add(new MenuItemDTO("ACOMP. EVOLUÇÃO PACIENTE", "#"));
                }
                // Tipo 3 (Supervisor homologa)
                if (checkPermissao(tipoProfi, "3")) {
                    menu.add(new MenuItemDTO("HOMOLOGAÇÃO ODONTO", "#"));
                }
                break;

            // --- BIOMEDICINA ---
            case "BIOMEDICINA":
                // Tipo 2 e 3
                if (checkPermissao(tipoProfi, "2", "3")) {
                    menu.add(new MenuItemDTO("RELATÓRIO DE PRODUÇÃO", "#"));
                    menu.add(new MenuItemDTO("CADASTRO DE RESULTADOS", "#"));
                    menu.add(new MenuItemDTO("CAD. PEDIDOS E PRESCRIÇÃO", "#"));
                    menu.add(new MenuItemDTO("COLETA DE ASSINATURAS", "#"));
                }
                // Tipo 3 (Exclusivo Supervisor)
                if (checkPermissao(tipoProfi, "3")) {
                    menu.add(new MenuItemDTO("IMPRESSÃO DE LAUDOS", "#"));
                    menu.add(new MenuItemDTO("EMISSÃO DE LAUDO", "#"));
                }
                break;

            // --- NUTRIÇÃO ---
            case "NUTRICAO":
                // Tipo 2 e 3
                if (checkPermissao(tipoProfi, "2", "3")) {
                    menu.add(new MenuItemDTO("AVALIAÇÃO NUTRICIONAL", "#"));
                    menu.add(new MenuItemDTO("ACOMP. EVOLUÇÃO PACIENTE", "#"));
                }
                // Tipo 3
                if (checkPermissao(tipoProfi, "3")) {
                    menu.add(new MenuItemDTO("HOMOLOGAÇÃO NUTRI", "#"));
                }
                break;

            // --- PSICOLOGIA ---
            case "PSICOLOGIA":
                // Tipo 2 e 3
                if (checkPermissao(tipoProfi, "2", "3")) {
                    menu.add(new MenuItemDTO("ACOMP. EVOLUÇÃO PACIENTE", "#"));
                    // Adicionei CAD. PRESCRIÇÃO se fizer sentido para Psico, senão remova
                    menu.add(new MenuItemDTO("CAD. PRESCRIÇÃO DO PACIENTE", "#")); 
                }
                // Tipo 3
                if (checkPermissao(tipoProfi, "3")) {
                    menu.add(new MenuItemDTO("HOMOLOGAÇÃO PSICO", "#"));
                }
                break;

            // --- FISIOTERAPIA ---
            case "FISIOTERAPIA":
                if (checkPermissao(tipoProfi, "2", "3")) {
                    menu.add(new MenuItemDTO("ACOMP. EVOLUÇÃO PACIENTE", "#"));
                    menu.add(new MenuItemDTO("CAD. PRESCRIÇÃO DO PACIENTE", "/pages/prescricao.html"));
                    
                    // --- NOVO BOTÃO ---
                    menu.add(new MenuItemDTO("PRONTUÁRIOS DEVOLVIDOS", "/pages/correcoes.html"));
                }
                // ... resto do código ...
                // Tipo 3
                if (checkPermissao(tipoProfi, "3")) {
                    menu.add(new MenuItemDTO("HOMOLOGAÇÃO FISIO", "/pages/homologacao.html"));
                }
                break;
            default:
                // Caso entre com um sistema não mapeado (Ex: Medicina)
                if (tipoProfi.equals("4")) { 
                   menu.add(new MenuItemDTO("Módulo em desenvolvimento: " + sistemaUpper, "#"));
                }
        }

        // =====================================================================
        // 4. RODAPÉ DO MASTER (TIPOPROFI = 4)
        // =====================================================================
        if (tipoProfi.equals("4")) {
            // Funcionalidade Global de Master
            menu.add(new MenuItemDTO("CAD. TRATAMENTO", "#")); 
            // Configurações
            menu.add(new MenuItemDTO("--- ADMINISTRAÇÃO ---", "#"));
            menu.add(new MenuItemDTO("CADASTRAR USUÁRIO", "/pages/cadastro-usuario.html"));
            menu.add(new MenuItemDTO("CONFIGURAÇÕES DO MÓDULO", "/pages/configuracoes.html"));
            menu.add(new MenuItemDTO("GESTÃO DA EQUIPE", "/pages/gestao-equipe.html"));
        }

        return ResponseEntity.ok(menu);
    }

    private boolean checkPermissao(String usuarioTipo, String... tiposPermitidos) {
        // REGRA DE OURO: O MASTER (4) tem acesso a TUDO do módulo dele.
        if (usuarioTipo.equals("4")) {
            return true;
        }
        
        for (String tipo : tiposPermitidos) {
            if (usuarioTipo.equals(tipo)) return true;
        }
        return false;
    }
}