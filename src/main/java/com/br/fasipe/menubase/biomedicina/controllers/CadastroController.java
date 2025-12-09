package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.dto.CadastroUsuarioDTO;
import com.br.fasipe.menubase.biomedicina.models.*;
import com.br.fasipe.menubase.biomedicina.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5500")
public class CadastroController {

    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private ProfissionalRepository profiRepo;
    @Autowired private PessoaRepository pessoaRepo;
    @Autowired private PessoaFisRepository pessoaFisRepo;
    @Autowired private EspecialidadeRepository especRepo;
    @Autowired private DocumentoRepository documentoRepo;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarUsuario(
            @RequestBody CadastroUsuarioDTO novoUser,
            @RequestParam String tipoProfiLogado,
            @RequestParam Integer idEspecLogado
    ) {
        
        System.out.println(">>> INICIANDO CADASTRO: " + novoUser.getNome());

        // 0. VERIFICAÇÃO PRÉVIA (EVITA ERRO DE DUPLICIDADE)
        if (usuarioRepo.findByLogin(novoUser.getLogin()).isPresent()) {
            return ResponseEntity.badRequest().body("O Login '" + novoUser.getLogin() + "' já está em uso. Escolha outro.");
        }

        // 1. REGRAS DE SEGURANÇA
        if ("1".equals(tipoProfiLogado)) {
            if ("4".equals(novoUser.getTipoProfi())) {
                int qtdMasters = profiRepo.contarMastersPorEspecialidade(novoUser.getIdEspec());
                if (qtdMasters > 0) return ResponseEntity.status(403).body("ERRO: Já existe um Master para esta especialidade.");
            } else if (!"1".equals(novoUser.getTipoProfi())) {
                return ResponseEntity.status(403).body("ERRO: Admin só cria Admins ou o PRIMEIRO Master.");
            }
        } else if ("4".equals(tipoProfiLogado)) {
            if (!novoUser.getIdEspec().equals(idEspecLogado)) return ResponseEntity.status(403).body("ERRO: Cadastro permitido apenas na sua especialidade.");
            if ("1".equals(novoUser.getTipoProfi())) return ResponseEntity.status(403).body("ERRO: Master não cria Administradores.");
        } else {
            return ResponseEntity.status(403).body("ERRO: Sem permissão.");
        }

        // 2. CPF
        Long idDocumentoNumerico = null;
        try {
            if (novoUser.getCpf() != null) {
                String cpfLimpo = novoUser.getCpf().replaceAll("\\D", "");
                idDocumentoNumerico = Long.parseLong(cpfLimpo);
            } else {
                return ResponseEntity.badRequest().body("O CPF é obrigatório.");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("CPF inválido.");
        }

        // --- 3. PERSISTÊNCIA MANUAL ---
        try {
            
            // A. DOCUMENTO (Se já existe, usamos o existente. Se não, cria.)
            if (!documentoRepo.existsById(idDocumentoNumerico)) {
                System.out.println(">>> CRIANDO NOVO DOCUMENTO: " + idDocumentoNumerico);
                documentoRepo.salvarDocumentoNaMarra(idDocumentoNumerico);
            } else {
                System.out.println(">>> DOCUMENTO JÁ EXISTE. REUTILIZANDO.");
            }

            // B. PESSOA
            System.out.println(">>> SALVANDO PESSOA...");
            Pessoa pessoa = new Pessoa();
            pessoa.setNome(novoUser.getNome());
            pessoa.setEmail(novoUser.getEmail());
            pessoa.setIdDocumento(idDocumentoNumerico);
            pessoa.setTelefone(novoUser.getTelefone());
            pessoa.setCep(novoUser.getCep());
            pessoa.setBairro(novoUser.getBairro());
            pessoa.setNumeroEndereco(novoUser.getNumeroEndereco());
            pessoa.setComplemento(novoUser.getComplemento());
            pessoa.setCidadeReside(novoUser.getCidadeReside());
            pessoa.setEstadoReside(novoUser.getEstadoReside());
            pessoa.setCidadeNascimento(novoUser.getCidadeNascimento());
            pessoa.setEstadoNascimento(novoUser.getEstadoNascimento());
            
            pessoa = pessoaRepo.save(pessoa);

            // C. PESSOA FÍSICA
            System.out.println(">>> SALVANDO PESSOA FÍSICA...");
            PessoaFis pf = new PessoaFis();
            int idManualPF = Math.abs(new Random().nextInt(2000000000));
            pf.setId(idManualPF);
            pf.setIdPessoa(pessoa.getIdPessoa());
            pf.setIdDocumento(idDocumentoNumerico);
            pf = pessoaFisRepo.save(pf);

            // D. PROFISSIONAL
            System.out.println(">>> SALVANDO PROFISSIONAL...");
            Profissional profi = new Profissional();
            profi.setTipoProfi(novoUser.getTipoProfi());
            profi.setIdDocumento(idDocumentoNumerico);
            
            // Conselho
            int idConselho = determinarIdConselho(novoUser.getIdEspec());
            profi.setIdConselho(idConselho); 
            
            if (novoUser.getIdEspec() != null) {
                Optional<Especialidade> espec = especRepo.findById(novoUser.getIdEspec());
                if (espec.isPresent()) {
                    java.util.Set<Especialidade> setEspec = new java.util.HashSet<>();
                    setEspec.add(espec.get());
                    profi.setEspecialidades(setEspec);
                }
            }
            profi = profiRepo.save(profi);

            // E. USUÁRIO
            System.out.println(">>> SALVANDO USUÁRIO...");
            Usuario usuario = new Usuario();
            usuario.setLogin(novoUser.getLogin());
            usuario.setSenha(novoUser.getSenha());
            usuario.setPessoaFis(pf);
            usuario.setProfissional(profi);
            usuario.setIdDocumento(idDocumentoNumerico);
            
            usuarioRepo.save(usuario);

            System.out.println(">>> SUCESSO TOTAL!");
            return ResponseEntity.ok("Usuário cadastrado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            // Pega erros de duplicidade que passaram pela verificação inicial
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                return ResponseEntity.badRequest().body("Erro: CPF ou Login já existem no banco de dados.");
            }
            return ResponseEntity.badRequest().body("Erro técnico: " + e.getMessage());
        }
    }

    private Integer determinarIdConselho(Integer idEspec) {
        if (idEspec == null) return 69;
        switch (idEspec) {
            case 6:  return 1; 
            case 9:  return 67;
            case 4:  return 61;
            case 5:  return 62;
            case 3:  return 60;
            case 10: return 69;
            default: return 69;
        }
    }
}