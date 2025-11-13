package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.models.Especialidade;
import com.br.fasipe.menubase.biomedicina.models.Profissional;
import com.br.fasipe.menubase.biomedicina.models.Usuario;
import com.br.fasipe.menubase.biomedicina.repository.UsuarioRepository;
import com.br.fasipe.menubase.biomedicina.dto.LoginResponse;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
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
            // Usuário ou senha incorretos
            return ResponseEntity.status(401).body(null); 
        }

        // --- USUÁRIO ENCONTRADO ---
        Usuario usuario = usuarioOpcional.get();
        
        // --- DEFINIR VALORES PADRÃO ---
        String nome = "Usuário";
        String cargo = "Profissional"; // Padrão
        String sistema = "GERAL"; // Padrão
        boolean isSystemAdmin = false;
        
        Profissional profissional = usuario.getProfissional();
        
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

        // 2. VERIFICAR SE É PROFISSIONAL
        if (profissional != null) {
            
            // 3. VERIFICAR SE É ADMIN DO SISTEMA (ID=1 e Espec=10)
            // (Conforme sua regra)
            final int ADMIN_ESPEC_ID = 10;
            if (profissional.getIdprofissio() == 1) {
                isSystemAdmin = profissional.getEspecialidades().stream()
                    .anyMatch(espec -> espec.getIdespec() == ADMIN_ESPEC_ID);
            }

            // 4. DEFINIR CARGO (Conforme sua nova regra)
            if (isSystemAdmin) {
                cargo = "Administrador";
                sistema = "TODOS";
            } else {
                // Se não for Admin, checa se é Supervisor ou Profissional
                if (profissional.getIdSupprofi() != null) {
                    cargo = "Supervisor"; // ID_SUPPROFI NÃO é nulo = Supervisor
                } else {
                    cargo = "Profissional"; // ID_SUPPROFI é nulo = Profissional
                }
                
                // 5. DEFINIR SISTEMA (Pega a primeira especialidade que não seja "Administrador")
                Optional<Especialidade> especPrincipal = profissional.getEspecialidades().stream()
                    .filter(espec -> espec.getIdespec() != ADMIN_ESPEC_ID)
                    .findFirst();

                if (especPrincipal.isPresent()) {
                    sistema = especPrincipal.get().getDescespec().toUpperCase();
                }
            }
        }
        
        // Cria e retorna o objeto de resposta
        LoginResponse response = new LoginResponse(nome, cargo, isSystemAdmin, sistema);
        return ResponseEntity.ok(response);
    }
}

// O DTO de LoginRequest continua o mesmo
class LoginRequest {
    private String login; 
    private String senha;
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}