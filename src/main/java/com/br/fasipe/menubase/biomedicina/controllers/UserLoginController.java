package com.br.fasipe.menubase.biomedicina.controllers;

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

        if (usuarioOpcional.isPresent()) {
            Usuario usuario = usuarioOpcional.get();
            
            String nome = "Usuário"; 
            String cargo = "Profissional"; 

            try {
                // --- CORREÇÃO AQUI ---
                // Trocado .getNome() por .getNomepessoa()
                if (usuario.getProfissional() != null && usuario.getProfissional().getPessoaFis() != null) {
                    nome = usuario.getProfissional().getPessoaFis().getNomepessoa();
                } else if (usuario.getPessoaFis() != null) {
                    // --- CORREÇÃO AQUI ---
                    nome = usuario.getPessoaFis().getNomepessoa();
                }
            } catch (Exception e) {
                System.err.println("Erro ao buscar nome: " + e.getMessage());
            }

            Optional<String> adminRole = usuario.getPermissoes().stream()
                .map(permissao -> permissao.getDescpermi().toLowerCase())
                .filter(desc -> desc.equals("administrador"))
                .findFirst();

            if (adminRole.isPresent()) {
                cargo = "Administrador";
            } else if (usuario.getProfissional() != null) {
                // --- CORREÇÃO AQUI ---
                // O método .getIdSupprofi() agora existe no Profissional.java
                if (usuario.getProfissional().getIdSupprofi() == null) {
                    cargo = "Supervisor";
                } else {
                    cargo = "Profissional";
                }
            }
            // --- FIM DA LÓGICA ---

            LoginResponse response = new LoginResponse(nome, cargo);
            return ResponseEntity.ok(response);

        } else {
            return ResponseEntity.status(401).body(null); 
        }
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