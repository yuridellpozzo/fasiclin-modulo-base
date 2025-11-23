package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.models.Especialidade;
import com.br.fasipe.menubase.biomedicina.models.Profissional;
import com.br.fasipe.menubase.biomedicina.models.Usuario;
import com.br.fasipe.menubase.biomedicina.repository.UsuarioRepository;
import com.br.fasipe.menubase.biomedicina.dto.LoginResponse;
import com.br.fasipe.menubase.biomedicina.dto.EspecialidadeDTO;

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

        // --- 1. BUSCA DO NOME (LÓGICA NOVA) ---
        String nome = usuario.getLogin().toUpperCase(); // Fallback
        
        try {
            // Verifica se o usuário tem o vínculo com PessoaFis
            if (usuario.getPessoaFis() != null) {
                // Pega o ID_PESSOA de dentro da PessoaFis
                Integer idPessoa = usuario.getPessoaFis().getIdPessoa();
                
                if (idPessoa != null) {
                    // Vai no banco buscar o nome na tabela PESSOA
                    String nomeBanco = usuarioRepository.findNomeByIdPessoa(idPessoa);
                    
                    if (nomeBanco != null && !nomeBanco.isEmpty()) {
                        nome = nomeBanco; // Sucesso!
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar nome na tabela PESSOA: " + e.getMessage());
        }
        // --------------------------------------

        // --- 2. VARIÁVEIS ---
        String cargo = "Indefinido";
        String tipoProfi = "0";
        boolean isSystemAdmin = false;
        String sistemaPrincipal = "INDEFINIDO"; 
        List<EspecialidadeDTO> especialidadesDTO = List.of();

        // --- 3. LÓGICA DE SISTEMA ---
        if (profissional != null) {
            tipoProfi = profissional.getTipoProfi(); 
            
            if (profissional.getEspecialidades() != null) {
                especialidadesDTO = profissional.getEspecialidades().stream()
                    .map(e -> new EspecialidadeDTO(e.getIdespec(), e.getDescespec()))
                    .collect(Collectors.toList());

                // Mapeamento
                for (EspecialidadeDTO esp : especialidadesDTO) {
                    Integer id = esp.getId();
                    if (id == 9) { sistemaPrincipal = "BIOMEDICINA"; break; }
                    else if (id == 4) { sistemaPrincipal = "ODONTOLOGIA"; break; }
                    else if (id == 5) { sistemaPrincipal = "NUTRICAO"; break; }
                    else if (id == 3) { sistemaPrincipal = "PSICOLOGIA"; break; }
                    else if (id == 6) { sistemaPrincipal = "FISIOTERAPIA"; break; }
                }
            }

            if (tipoProfi != null) {
                switch (tipoProfi) {
                    case "1": cargo = "Administrador"; isSystemAdmin = true; sistemaPrincipal = "CORINGA"; break;
                    case "2": cargo = "Profissional"; break;
                    case "3": cargo = "Supervisor"; break;
                    case "4": cargo = "Master"; break;
                    default: cargo = "Profissional";
                }
            }
        }
        
        if (sistemaPrincipal.equals("INDEFINIDO")) sistemaPrincipal = "BIOMEDICINA"; 

        System.out.println("LOGIN -> Nome: " + nome + " | Sistema: " + sistemaPrincipal);

        return ResponseEntity.ok(new LoginResponse(nome, cargo, isSystemAdmin, sistemaPrincipal, tipoProfi, especialidadesDTO));
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