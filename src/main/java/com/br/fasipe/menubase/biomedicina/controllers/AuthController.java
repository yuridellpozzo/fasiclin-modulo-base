package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.dto.LoginDTO;
import com.br.fasipe.menubase.biomedicina.dto.LoginResponseDTO;
import com.br.fasipe.menubase.biomedicina.dto.RedefinirSenhaDTO;
import com.br.fasipe.menubase.biomedicina.models.Especialidade;
import com.br.fasipe.menubase.biomedicina.models.Pessoa;
import com.br.fasipe.menubase.biomedicina.models.Usuario;
import com.br.fasipe.menubase.biomedicina.repository.PessoaRepository;
import com.br.fasipe.menubase.biomedicina.repository.UsuarioRepository;
import com.br.fasipe.menubase.biomedicina.services.RecuperacaoSenhaService;
import com.br.fasipe.menubase.biomedicina.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5500")
public class AuthController {

    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private TokenService tokenService;
    @Autowired private RecuperacaoSenhaService recuperacaoService;
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

            String nomeCargo = "Usuário";
            if ("1".equals(tipoCodigo)) nomeCargo = "Administrador Geral";
            else if ("2".equals(tipoCodigo)) nomeCargo = "Estagiário / Profissional";
            else if ("3".equals(tipoCodigo)) nomeCargo = "Supervisor";
            else if ("4".equals(tipoCodigo)) nomeCargo = "Master / Coordenador";

            String sistema = "SEM VINCULO"; 
            List<String> listaEspec = new ArrayList<>();

            // REGRA: Admin vê tudo
            if ("1".equals(tipoCodigo)) {
                sistema = "ADMINISTRADOR"; 
                listaEspec.add("BIOMEDICINA"); listaEspec.add("FISIOTERAPIA"); 
                listaEspec.add("ODONTOLOGIA"); listaEspec.add("NUTRICAO"); listaEspec.add("PSICOLOGIA");
            } 
            // REGRA: Outros (Inclusive Master) buscam do banco, mas HIGIENIZADO
            else if (user.getProfissional() != null) {
                Set<Especialidade> especialidades = user.getProfissional().getEspecialidades();
                if (especialidades != null && !especialidades.isEmpty()) {
                    
                    // Pega o primeiro e LIMPA (Nutrição -> NUTRICAO)
                    sistema = higienizarTexto(especialidades.iterator().next().getNome());
                    
                    for (Especialidade e : especialidades) {
                        // Limpa cada especialidade da lista
                        listaEspec.add(higienizarTexto(e.getNome()));
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

            // LOG
            System.out.println("---------------------------------------------------------");
            System.out.println("LOGIN SUCESSO -> Usuário: " + nomeExibicao);
            System.out.println("              -> Cargo:   " + nomeCargo);
            System.out.println("              -> Sistema: " + sistema); // Agora vai aparecer sem acento!
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
    
    // --- FUNÇÃO DE LIMPEZA (REMOVE ACENTOS E Ç) ---
    private String higienizarTexto(String texto) {
        if (texto == null) return "";
        
        // 1. Remove acentos (Normalizer separa 'ã' em 'a' + '~')
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String semAcento = pattern.matcher(normalizado).replaceAll("");
        
        // 2. Garante Maiúsculas e remove espaços extras
        String limpo = semAcento.toUpperCase().trim();
        
        // 3. Ajustes manuais de segurança (caso o Normalizer falhe em algum ambiente)
        limpo = limpo.replace("Ç", "C");
        
        // 4. (Opcional) Se Psicologia estiver vindo como "PSICOLOGIA CLINICA", cortamos para pegar só a primeira palavra?
        // Descomente a linha abaixo se quiser forçar pegar só a primeira palavra (ex: "ENGENHARIA CIVIL" -> "ENGENHARIA")
        // if (limpo.contains(" ")) limpo = limpo.split(" ")[0];

        return limpo;
    }

    // --- ATUALIZAÇÃO: BUSCA POR LOGIN OU EMAIL ---
    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> solicitarToken(@RequestBody String loginOuEmail) {
        // Limpa as aspas que o JSON as vezes manda
        String textoLimpo = loginOuEmail.replace("\"", "").trim();
        
        // Usa o novo método do repositório que busca nas duas colunas
        Optional<Usuario> userOpt = usuarioRepo.findByLoginOrEmail(textoLimpo);
        
        if (userOpt.isPresent()) {
            String codigo = recuperacaoService.gerarCodigo(userOpt.get().getLogin());
            System.out.println("\n##################################################");
            System.out.println(">>> RECUPERAÇÃO DE SENHA SOLICITADA <<<");
            System.out.println(">>> CÓDIGO GERADO: " + codigo + " <<<");
            System.out.println("##################################################\n");
            return ResponseEntity.ok("Código enviado.");
        }
        
        return ResponseEntity.badRequest().body("Usuário ou E-mail não encontrado no sistema.");
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(@RequestBody RedefinirSenhaDTO dados) {
        String loginUser = recuperacaoService.validarCodigo(dados.getToken());
        if (loginUser == null) return ResponseEntity.badRequest().body("Código inválido.");
        if (loginUser.equals("EXPIRED")) return ResponseEntity.badRequest().body("Código expirado.");
        
        Optional<Usuario> userOpt = usuarioRepo.findByLogin(loginUser);
        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            user.setSenha(dados.getNovaSenha()); 
            usuarioRepo.save(user);              
            recuperacaoService.queimarCodigo(dados.getToken());
            System.out.println(">>> SENHA ALTERADA PARA: " + loginUser);
            return ResponseEntity.ok("Sucesso!");
        }
        return ResponseEntity.badRequest().body("Erro.");
    }
}