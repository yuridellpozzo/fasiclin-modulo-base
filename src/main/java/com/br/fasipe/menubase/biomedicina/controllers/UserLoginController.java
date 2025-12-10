/* 
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

        // --- CORREÇÃO NA BUSCA DO NOME ---
        String nome = usuario.getLogin().toUpperCase(); // Nome padrão (Login)
        Integer idPessoaParaBusca = null;

        try {
            // 1. Tenta pegar o ID_PESSOA através do usuário -> PessoaFis
            if (usuario.getPessoaFis() != null) {
                idPessoaParaBusca = usuario.getPessoaFis().getIdPessoa();
                // OBS: Não chamamos mais .getNomepessoa() aqui pois o campo não existe mais na classe
            }
            
            // 2. Se achou o ID, vai no banco buscar o NOME na tabela PESSOA
            if (idPessoaParaBusca != null) {
                String nomeBanco = usuarioRepository.findNomeByIdPessoa(idPessoaParaBusca);
                if (nomeBanco != null && !nomeBanco.isEmpty()) {
                    nome = nomeBanco; // SUCESSO!
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar nome real: " + e.getMessage());
        }

        // --- VARIÁVEIS DE RESPOSTA ---
        String cargo = "Indefinido";
        String tipoProfi = "0";
        boolean isSystemAdmin = false;
        String sistemaPrincipal = "INDEFINIDO"; 
        List<EspecialidadeDTO> especialidadesDTO = List.of();

        if (profissional != null) {
            tipoProfi = profissional.getTipoProfi(); 
            
            if (profissional.getEspecialidades() != null) {
                especialidadesDTO = profissional.getEspecialidades().stream()
                    .map(e -> new EspecialidadeDTO(e.getIdespec(), e.getDescespec()))
                    .collect(Collectors.toList());

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

        System.out.println("LOGIN SUCESSO -> Nome: " + nome + " | Sistema: " + sistemaPrincipal);

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
*/