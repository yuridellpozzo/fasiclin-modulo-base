package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.dto.LoginDTO;
import com.br.fasipe.menubase.biomedicina.dto.LoginResponseDTO;
import com.br.fasipe.menubase.biomedicina.models.Especialidade;
import com.br.fasipe.menubase.biomedicina.models.Pessoa;
import com.br.fasipe.menubase.biomedicina.models.Usuario;
import com.br.fasipe.menubase.biomedicina.repository.PessoaRepository;
import com.br.fasipe.menubase.biomedicina.repository.UsuarioRepository;
import com.br.fasipe.menubase.biomedicina.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5500")
public class AuthController {

    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private TokenService tokenService;
    @Autowired private PessoaRepository pessoaRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO data) {
        Optional<Usuario> userOpt = usuarioRepo.findByLogin(data.getLogin());

        if (userOpt.isPresent() && userOpt.get().getSenha().equals(data.getSenha())) {
            Usuario user = userOpt.get();
            String token = tokenService.gerarToken(user.getLogin());
            
            Long idProfi = null;
            String tipoCodigo = "2"; 
            
            if (user.getProfissional() != null) {
                idProfi = Long.valueOf(user.getProfissional().getId());
                tipoCodigo = user.getProfissional().getTipoProfi();
            }

            // 1. DEFINIR CARGO
            String nomeCargo = "Usuário";
            if ("1".equals(tipoCodigo)) nomeCargo = "Administrador Geral";
            else if ("2".equals(tipoCodigo)) nomeCargo = "Estagiário / Profissional";
            else if ("3".equals(tipoCodigo)) nomeCargo = "Supervisor";
            else if ("4".equals(tipoCodigo)) nomeCargo = "Master / Coordenador";

            // 2. DEFINIR SISTEMA / ESPECIALIDADE (AQUI ESTÁ A LÓGICA DO LOG)
            String sistema = "SEM VÍNCULO"; 
            List<String> listaEspec = new ArrayList<>();

            // Se for ADMIN (1)
            if ("1".equals(tipoCodigo)) {
                sistema = "ADMINISTRADOR"; // <--- Vai aparecer no terminal
                // Adiciona tudo pois admin pode tudo
                listaEspec.add("BIOMEDICINA"); listaEspec.add("FISIOTERAPIA"); 
                listaEspec.add("ODONTOLOGIA"); listaEspec.add("NUTRICAO"); listaEspec.add("PSICOLOGIA");
            } 
            // Se for MASTER (4)
            else if ("4".equals(tipoCodigo)) {
                sistema = "MASTER (SELEÇÃO)"; // <--- Vai aparecer no terminal
                listaEspec.add("BIOMEDICINA"); listaEspec.add("FISIOTERAPIA"); 
                listaEspec.add("ODONTOLOGIA"); listaEspec.add("NUTRICAO"); listaEspec.add("PSICOLOGIA");
            } 
            // Se for PROFISSIONAL ou SUPERVISOR (2 ou 3)
            else if (user.getProfissional() != null) {
                Set<Especialidade> especialidades = user.getProfissional().getEspecialidades();
                if (especialidades != null && !especialidades.isEmpty()) {
                    // Pega o nome exato do banco (Ex: FISIOTERAPIA)
                    sistema = especialidades.iterator().next().getNome().toUpperCase();
                    for (Especialidade e : especialidades) {
                        listaEspec.add(e.getNome());
                    }
                }
            }

            // 3. BUSCAR NOME REAL
            String nomeExibicao = user.getLogin(); 
            try {
                if (user.getPessoaFis() != null && user.getPessoaFis().getIdPessoa() != null) {
                    Integer idPessoa = user.getPessoaFis().getIdPessoa();
                    Optional<Pessoa> p = pessoaRepo.findById(idPessoa);
                    if (p.isPresent()) nomeExibicao = p.get().getNome();
                }
            } catch (Exception e) { }

            // --- LOG NO TERMINAL ---
            System.out.println("---------------------------------------------------------");
            System.out.println("LOGIN SUCESSO -> Usuário: " + nomeExibicao);
            System.out.println("              -> Cargo:   " + nomeCargo);
            System.out.println("              -> Sistema: " + sistema); // Aqui mostra: FISIOTERAPIA, BIOMEDICINA, MASTER...
            System.out.println("---------------------------------------------------------");

            return ResponseEntity.ok(new LoginResponseDTO(
                token,
                nomeExibicao,  
                nomeCargo,     
                tipoCodigo,    
                sistema,
                "1".equals(tipoCodigo),
                listaEspec,
                idProfi
            ));
        }
        return ResponseEntity.status(401).body("Usuário ou senha inválidos.");
    }
    
    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> solicitarToken(@RequestBody String email) { return ResponseEntity.ok("Token enviado."); }
    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@RequestBody Object dados) { return ResponseEntity.ok("Senha alterada."); }
}