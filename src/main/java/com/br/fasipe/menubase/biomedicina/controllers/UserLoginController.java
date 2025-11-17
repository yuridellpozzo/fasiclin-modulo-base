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

        // --- VARIÁVEIS PADRÃO ---
        String nome = "Usuário";
        String cargo = "Indefinido";
        String tipoProfi = "0";
        boolean isSystemAdmin = false;
        String sistemaPrincipal = "INDEFINIDO"; // Variável nova para resolver o erro do Supervisor
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

        // 2. LÓGICA BASEADA EM TIPOPROFI
        if (profissional != null) {
            tipoProfi = profissional.getTipoProfi(); 
            
            // Converter especialidades para DTO
            especialidadesDTO = profissional.getEspecialidades().stream()
                .map(e -> new EspecialidadeDTO(e.getIdespec(), e.getDescespec()))
                .collect(Collectors.toList());

            // --- CORREÇÃO PARA O SUPERVISOR (E PROFISSIONAL) ---
            // Tenta encontrar o nome do sistema (ex: "BIOMEDICINA") na lista de especialidades
            Optional<Especialidade> especEncontrada = profissional.getEspecialidades().stream()
                .filter(e -> e.getIdespec() != 10) // Ignora "Administrador" (ID 10)
                .findFirst();

            if (especEncontrada.isPresent()) {
                sistemaPrincipal = especEncontrada.get().getDescespec().toUpperCase();
            }
            // ---------------------------------------------------

            if (tipoProfi != null) {
                switch (tipoProfi) {
                    case "1": 
                        cargo = "Administrador";
                        isSystemAdmin = true; 
                        sistemaPrincipal = "CORINGA"; // Admin sempre cai no Coringa
                        break;
                    case "2": 
                        cargo = "Profissional";
                        break;
                    case "3": 
                        cargo = "Supervisor";
                        break;
                    case "4": 
                        cargo = "Master";
                        break;
                    default:
                        cargo = "Profissional";
                }
            }
        }
        
        // Agora passamos o 'sistemaPrincipal' corretamente para o DTO
        LoginResponse response = new LoginResponse(nome, cargo, isSystemAdmin, sistemaPrincipal, tipoProfi, especialidadesDTO);
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