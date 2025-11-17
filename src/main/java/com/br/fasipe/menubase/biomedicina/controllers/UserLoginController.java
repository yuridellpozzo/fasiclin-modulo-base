package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.models.Profissional;
import com.br.fasipe.menubase.biomedicina.models.Usuario;
import com.br.fasipe.menubase.biomedicina.repository.UsuarioRepository;
import com.br.fasipe.menubase.biomedicina.dto.LoginResponse;
import com.br.fasipe.menubase.biomedicina.dto.EspecialidadeDTO; // Importe o novo DTO

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5500") 
@RestController
@RequestMapping("/api/auth")
public class UserLoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> autenticar(@RequestBody LoginRequest loginRequest) {
        
        Optional<Usuario> usuarioOpcional = usuarioRepository.findByLoginAndSenha(
            loginRequest.getLogin(), 
            loginRequest.getSenha()
        );

        if (usuarioOpcional.isEmpty()) {
            return ResponseEntity.status(401).body(null); 
        }

        Usuario usuario = usuarioOpcional.get();
        Profissional profissional = usuario.getProfissional();

        // --- VARIÁVEIS PADRÃO ---
        String nome = "Usuário";
        String cargo = "Indefinido";
        String tipoProfi = "0";
        boolean isSystemAdmin = false;
        List<EspecialidadeDTO> especialidadesDTO = List.of();

        // 1. OBTER NOME
        try {
            if (profissional != null && profissional.getPessoaFis() != null) {
                nome = profissional.getPessoaFis().getNomepessoa();
            } else if (usuario.getPessoaFis() != null) {
                nome = usuario.getPessoaFis().getNomepessoa();
            }
        } catch (Exception e) {
             System.err.println("Erro ao buscar nome: " + e.getMessage());
        }

        // 2. LÓGICA BASEADA EM TIPOPROFI (Passo 1 - Refatorado)
        if (profissional != null) {
            tipoProfi = profissional.getTipoProfi(); // Pega o '1', '2', '3' ou '4'
            
            // Converter especialidades para DTO
            especialidadesDTO = profissional.getEspecialidades().stream()
                .map(e -> new EspecialidadeDTO(e.getIdespec(), e.getDescespec()))
                .collect(Collectors.toList());

            // Define Cargo e Permissões com base no TIPOPROFI
            if (tipoProfi != null) {
                switch (tipoProfi) {
                    case "1": // ADMINISTRADOR (Coringa)
                        cargo = "Administrador";
                        isSystemAdmin = true; 
                        break;
                    case "2": // PROFISSIONAL
                        cargo = "Profissional";
                        break;
                    case "3": // SUPERVISOR
                        cargo = "Supervisor";
                        break;
                    case "4": // MASTER (Super Admin do Módulo)
                        cargo = "Master";
                        // Master não é admin global, mas tem poderes no módulo
                        break;
                    default:
                        cargo = "Profissional";
                }
            }
        }
        
        // Cria e retorna o objeto de resposta com os novos dados
        LoginResponse response = new LoginResponse(nome, cargo, isSystemAdmin, tipoProfi, especialidadesDTO);
        return ResponseEntity.ok(response);
    }
}

class LoginRequest {
    private String login; 
    private String senha;
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}