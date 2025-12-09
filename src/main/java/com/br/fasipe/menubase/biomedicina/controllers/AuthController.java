package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.models.Usuario;
import com.br.fasipe.menubase.biomedicina.repository.UsuarioRepository;
import com.br.fasipe.menubase.biomedicina.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5500")
public class AuthController {

    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private TokenService tokenService;

    // 1. PEDIR CÓDIGO
    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> solicitarToken(@RequestBody String email) {
        // Busca usuário pelo e-mail (usando a query que criamos no Repo)
        Optional<Usuario> userOpt = usuarioRepo.findByEmailPessoa(email);

        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            String token = tokenService.gerarToken(user.getLogin());

            // SIMULAÇÃO DE EMAIL NO CONSOLE
            System.out.println(">>> RECUPERAÇÃO DE SENHA <<<");
            System.out.println("Para: " + email);
            System.out.println("Seu código é: " + token);
            System.out.println(">>> -------------------- <<<");

            return ResponseEntity.ok("Código enviado! (Verifique o console do servidor)");
        }
        return ResponseEntity.badRequest().body("E-mail não encontrado.");
    }

    // 2. TROCAR SENHA
    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@RequestBody RedefinirDTO dados) {
        String login = tokenService.validarToken(dados.getToken());

        if (login == null) return ResponseEntity.badRequest().body("Código inválido.");
        if (login.equals("EXPIRED")) return ResponseEntity.badRequest().body("Código expirado.");

        Optional<Usuario> userOpt = usuarioRepo.findByLogin(login);
        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            user.setSenha(dados.getNovaSenha());
            usuarioRepo.save(user); // Salva nova senha
            
            tokenService.queimarToken(dados.getToken()); // Invalida o código
            return ResponseEntity.ok("Senha alterada com sucesso!");
        }
        return ResponseEntity.badRequest().body("Erro ao processar.");
    }
}

// DTO Auxiliar interno
class RedefinirDTO {
    private String token;
    private String novaSenha;
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getNovaSenha() { return novaSenha; }
    public void setNovaSenha(String novaSenha) { this.novaSenha = novaSenha; }
}