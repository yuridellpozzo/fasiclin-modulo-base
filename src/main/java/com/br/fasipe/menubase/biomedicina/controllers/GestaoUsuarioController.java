package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.dto.UsuarioResumoDTO;
import com.br.fasipe.menubase.biomedicina.models.PessoaFis;
import com.br.fasipe.menubase.biomedicina.models.Profissional;
import com.br.fasipe.menubase.biomedicina.models.Usuario;
import com.br.fasipe.menubase.biomedicina.repository.PessoaFisRepository;
import com.br.fasipe.menubase.biomedicina.repository.PessoaRepository;
import com.br.fasipe.menubase.biomedicina.repository.ProfissionalRepository;
import com.br.fasipe.menubase.biomedicina.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/gestao")
@CrossOrigin(origins = "http://localhost:5500")
public class GestaoUsuarioController {

    @Autowired private ProfissionalRepository profiRepo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private PessoaFisRepository pfRepo;
    @Autowired private PessoaRepository pessoaRepo;

    @GetMapping("/listar")
    public ResponseEntity<?> listarEquipe(
            @RequestParam String tipoProfiLogado,
            @RequestParam Integer idEspecLogado
    ) {
        Integer idBusca = idEspecLogado;
        if ("1".equals(tipoProfiLogado)) idBusca = 10;
        List<UsuarioResumoDTO> lista = profiRepo.listarPorEspecialidade(idBusca);
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/alterar-cargo/{idProfissional}")
    public ResponseEntity<String> alterarCargo(@PathVariable Integer idProfissional, @RequestBody Map<String, String> payload) {
        String novoTipo = payload.get("novoTipo");
        Optional<Profissional> profiOpt = profiRepo.findById(idProfissional);
        if (profiOpt.isPresent()) {
            Profissional p = profiOpt.get();
            p.setTipoProfi(novoTipo);
            profiRepo.save(p);
            return ResponseEntity.ok("Cargo alterado com sucesso!");
        }
        return ResponseEntity.badRequest().body("Profissional não encontrado.");
    }

    // --- DELETE VIA SQL NATIVO (Solução Definitiva) ---
    @DeleteMapping("/excluir/{idUsuario}")
    public ResponseEntity<String> excluirUsuario(@PathVariable Integer idUsuario) {
        try {
            // 1. Apenas buscamos para pegar os IDs (Leitura)
            Optional<Usuario> userOpt = usuarioRepo.findById(idUsuario);
            
            if (userOpt.isPresent()) {
                Usuario u = userOpt.get();
                
                // Guardamos os IDs necessários
                Integer idProfissional = (u.getProfissional() != null) ? u.getProfissional().getId() : null;
                Integer idPessoaFis = (u.getPessoaFis() != null) ? u.getPessoaFis().getId() : null;
                Integer idPessoa = (u.getPessoaFis() != null) ? u.getPessoaFis().getIdPessoa() : null;

                // 2. EXECUTAMOS OS DELETES NATIVOS NA ORDEM (Filho -> Pai)
                
                // A. Limpa vínculos da especialidade
                if (idProfissional != null) {
                    profiRepo.desvincularEspecialidades(idProfissional);
                }

                // B. Remove o Usuário (Que segura as FKs)
                usuarioRepo.deletarPorIdNativo(idUsuario);

                // C. Remove o Profissional
                if (idProfissional != null) {
                    profiRepo.deletarPorIdNativo(idProfissional);
                }

                // D. Remove a Pessoa Física
                if (idPessoaFis != null) {
                    pfRepo.deletarPorIdNativo(idPessoaFis);
                }

                // E. Remove a Pessoa (Opcional - Limpeza total)
                if (idPessoa != null) {
                    pessoaRepo.deletarPorIdNativo(idPessoa);
                }
                
                return ResponseEntity.ok("Usuário excluído com sucesso.");
            }
            return ResponseEntity.badRequest().body("Usuário não encontrado.");

        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("ConstraintViolation")) {
                return ResponseEntity.badRequest().body("Não é possível excluir: Existem registros presos a este usuário.");
            }
            return ResponseEntity.badRequest().body("Erro ao excluir: " + e.getMessage());
        }
    }
}