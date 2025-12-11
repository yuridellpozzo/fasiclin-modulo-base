package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.dto.LoginDTO;
import com.br.fasipe.menubase.biomedicina.dto.LoginResponseDTO;
import com.br.fasipe.menubase.biomedicina.dto.RedefinirSenhaDTO; // DTO para senha
import com.br.fasipe.menubase.biomedicina.models.Especialidade;
import com.br.fasipe.menubase.biomedicina.models.Pessoa;
import com.br.fasipe.menubase.biomedicina.models.Usuario;
import com.br.fasipe.menubase.biomedicina.repository.PessoaRepository;
import com.br.fasipe.menubase.biomedicina.repository.UsuarioRepository;
import com.br.fasipe.menubase.biomedicina.services.RecuperacaoSenhaService; // Service de Recuperação
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
    @Autowired private TokenService tokenService; // JWT Login
    @Autowired private PessoaRepository pessoaRepo;
    @Autowired private RecuperacaoSenhaService recuperacaoService; // <--- INJEÇÃO NOVA

    // --- 1. LOGIN (MANTIDO EXATAMENTE COMO VOCÊ ENVIOU) ---
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

            String nomeCargo = "Usuário";
            if ("1".equals(tipoCodigo)) nomeCargo = "Administrador Geral";
            else if ("2".equals(tipoCodigo)) nomeCargo = "Estagiário / Profissional";
            else if ("3".equals(tipoCodigo)) nomeCargo = "Supervisor";
            else if ("4".equals(tipoCodigo)) nomeCargo = "Master / Coordenador";

            String sistema = "SEM VÍNCULO"; 
            List<String> listaEspec = new ArrayList<>();

            // REGRA ORIGINAL: Apenas ADMIN (1) vê tudo. Master (4) segue regra do banco.
            if ("1".equals(tipoCodigo)) {
                sistema = "ADMINISTRADOR"; 
                listaEspec.add("BIOMEDICINA"); listaEspec.add("FISIOTERAPIA"); 
                listaEspec.add("ODONTOLOGIA"); listaEspec.add("NUTRICAO"); listaEspec.add("PSICOLOGIA");
            } 
            else if (user.getProfissional() != null) {
                Set<Especialidade> especialidades = user.getProfissional().getEspecialidades();
                if (especialidades != null && !especialidades.isEmpty()) {
                    sistema = especialidades.iterator().next().getNome().toUpperCase();
                    for (Especialidade e : especialidades) {
                        listaEspec.add(e.getNome());
                    }
                }
            }

            String nomeExibicao = user.getLogin(); 
            try {
                if (user.getPessoaFis() != null && user.getPessoaFis().getIdPessoa() != null) {
                    Integer idPessoa = user.getPessoaFis().getIdPessoa();
                    Optional<Pessoa> p = pessoaRepo.findById(idPessoa);
                    if (p.isPresent()) nomeExibicao = p.get().getNome();
                }
            } catch (Exception e) { }

            // LOG NO TERMINAL
            System.out.println("---------------------------------------------------------");
            System.out.println("LOGIN SUCESSO -> Usuário: " + nomeExibicao);
            System.out.println("              -> Cargo:   " + nomeCargo);
            System.out.println("              -> Sistema: " + sistema);
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
    
    // --- 2. SOLICITAR CÓDIGO (USANDO O SERVICE NOVO) ---
    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> solicitarToken(@RequestBody String loginOuEmail) {
        String loginLimpo = loginOuEmail.replace("\"", "").trim();
        Optional<Usuario> userOpt = usuarioRepo.findByLogin(loginLimpo);
        
        if (userOpt.isPresent()) {
            // Gera o código e guarda na memória do Service
            String codigo = recuperacaoService.gerarCodigo(userOpt.get().getLogin());
            
            System.out.println("\n##################################################");
            System.out.println(">>> RECUPERAÇÃO DE SENHA SOLICITADA <<<");
            System.out.println(">>> CÓDIGO GERADO: " + codigo + " <<<");
            System.out.println("##################################################\n");

            return ResponseEntity.ok("Código enviado para o terminal do servidor.");
        }
        return ResponseEntity.badRequest().body("Usuário não encontrado.");
    }

    // --- 3. GRAVAR NOVA SENHA NO BANCO ---
    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@RequestBody RedefinirSenhaDTO dados) {
        
        // 1. Valida se o código existe e pega o login do dono
        String loginUser = recuperacaoService.validarCodigo(dados.getToken());

        if (loginUser == null) return ResponseEntity.badRequest().body("Código inválido.");
        if (loginUser.equals("EXPIRED")) return ResponseEntity.badRequest().body("Código expirado.");
        
        // 2. Busca o usuário no banco
        Optional<Usuario> userOpt = usuarioRepo.findByLogin(loginUser);
        
        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            
            // 3. ALTERA A SENHA E SALVA NO BANCO
            user.setSenha(dados.getNovaSenha()); 
            usuarioRepo.save(user); // <--- O UPDATE ACONTECE AQUI
            
            // 4. Queima o código para não usar de novo
            recuperacaoService.queimarCodigo(dados.getToken());
            
            System.out.println(">>> SUCESSO: Senha alterada no banco para: " + loginUser);
            return ResponseEntity.ok("Senha alterada com sucesso!");
        }
        
        return ResponseEntity.badRequest().body("Erro ao encontrar usuário.");
    }
}