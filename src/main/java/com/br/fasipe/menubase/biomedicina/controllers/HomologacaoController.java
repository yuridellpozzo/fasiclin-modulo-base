package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.dto.ProntuarioDTO;
import com.br.fasipe.menubase.biomedicina.models.Prontuario;
import com.br.fasipe.menubase.biomedicina.models.ProntuarioTemporario;
import com.br.fasipe.menubase.biomedicina.models.Profissional;
import com.br.fasipe.menubase.biomedicina.repository.ProntuarioRepository;
import com.br.fasipe.menubase.biomedicina.repository.ProntuarioTemporarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/homologacao")
@CrossOrigin(origins = "http://localhost:5500")
public class HomologacaoController {

    @Autowired private ProntuarioTemporarioRepository tempRepo;
    @Autowired private ProntuarioRepository oficialRepo;

    // --- SALVAR (COM LÓGICA DE ATUALIZAÇÃO PARA NÃO DUPLICAR) ---
    @PostMapping("/salvar")
    public ResponseEntity<String> salvarTemporario(@RequestBody ProntuarioDTO dto) {
        try {
            if (dto.getIdProfissional() == null) {
                return ResponseEntity.badRequest().body("Erro: ID Profissional não identificado.");
            }

            ProntuarioTemporario pt;

            // 1. VERIFICA SE É EDIÇÃO OU CRIAÇÃO
            if (dto.getId() != null) {
                // Se o Front mandou um ID, tentamos buscar no banco
                Optional<ProntuarioTemporario> existente = tempRepo.findById(dto.getId());
                if (existente.isPresent()) {
                    pt = existente.get(); // PEGA O ANTIGO (Não cria novo!)
                } else {
                    pt = new ProntuarioTemporario(); // Se não achar, cria novo
                }
            } else {
                pt = new ProntuarioTemporario(); // Se não tem ID, é novo
            }

            // 2. ATUALIZA OS DADOS (Seja no novo ou no antigo)
            Profissional p = new Profissional();
            p.setId(dto.getIdProfissional());
            pt.setAluno(p);

            pt.setIdPaciente(dto.getIdPaciente());
            pt.setIdProced(dto.getIdProced());
            pt.setIdEspec(dto.getIdEspec());
            pt.setTexto(dto.getTexto());
            pt.setLinkProced(dto.getLinkProcedimento()); 
            pt.setAutoPacVisu(dto.getAutoPacVisu());
            
            // Força o status voltar para PENDENTE (Para o supervisor ver de novo)
            pt.setStatus("PENDENTE");
            
            // Mantém a data original se for edição, ou põe hoje se for novo
            if (pt.getDataProced() == null) {
                pt.setDataProced(LocalDate.now());
            }

            tempRepo.save(pt); // O Hibernate faz UPDATE se tiver ID, ou INSERT se não tiver.
            
            return ResponseEntity.ok("Prontuário salvo com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro técnico: " + e.getMessage());
        }
    }

    // --- OUTROS MÉTODOS (Listar, Aprovar, Corrigir) - Mantenha igual ---
    @GetMapping("/pendentes/{idEspec}")
    public ResponseEntity<List<ProntuarioTemporario>> listarPendentes(@PathVariable Integer idEspec) {
        return ResponseEntity.ok(tempRepo.findPendentesPorEspecialidade(idEspec));
    }

    @PostMapping("/aprovar/{idTemp}")
    public ResponseEntity<String> aprovar(@PathVariable Integer idTemp, @RequestParam Integer idSupervisor) {
        Optional<ProntuarioTemporario> opt = tempRepo.findById(idTemp);
        if (opt.isPresent()) {
            ProntuarioTemporario temp = opt.get();
            Prontuario oficial = new Prontuario();
            
            oficial.setIdPaciente(temp.getIdPaciente());
            if (temp.getAluno() != null) oficial.setIdProfissio(temp.getAluno().getId());
            oficial.setIdEspec(temp.getIdEspec());
            oficial.setIdProced(temp.getIdProced());
            oficial.setDataProced(temp.getDataProced());
            oficial.setDescrProntu(temp.getTexto());
            oficial.setAutoPacVisu(temp.getAutoPacVisu());
            
            oficialRepo.save(oficial); 

            temp.setStatus("APROVADO");
            temp.setIdSupervisor(idSupervisor); 
            temp.setDataDecisao(LocalDateTime.now()); 
            temp.setObservacaoSupervisor("Aprovado.");
            tempRepo.save(temp);
            return ResponseEntity.ok("Aprovado!");
        }
        return ResponseEntity.badRequest().body("Não encontrado.");
    }

    @PostMapping("/corrigir/{idTemp}")
    public ResponseEntity<String> corrigir(@PathVariable Integer idTemp, @RequestParam Integer idSupervisor, @RequestBody String motivo) {
        Optional<ProntuarioTemporario> opt = tempRepo.findById(idTemp);
        if (opt.isPresent()) {
            ProntuarioTemporario temp = opt.get();
            temp.setStatus("REPROVADO"); 
            temp.setIdSupervisor(idSupervisor);
            temp.setDataDecisao(LocalDateTime.now());
            temp.setObservacaoSupervisor(motivo); 
            tempRepo.save(temp);
            return ResponseEntity.ok("Devolvido.");
        }
        return ResponseEntity.badRequest().body("Erro.");
    }
    
    @GetMapping("/correcoes/{idAluno}")
    public ResponseEntity<List<ProntuarioTemporario>> listarCorrecoes(@PathVariable Integer idAluno) {
        return ResponseEntity.ok(tempRepo.findReprovadosPorAluno(idAluno));
    }
}