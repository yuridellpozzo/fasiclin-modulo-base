package com.br.fasipe.menubase.biomedicina.controllers;

import com.br.fasipe.menubase.biomedicina.models.Prontuario;
import com.br.fasipe.menubase.biomedicina.models.ProntuarioTemporario;
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

    @Autowired
    private ProntuarioTemporarioRepository tempRepo;

    @Autowired
    private ProntuarioRepository oficialRepo;

    // 1. LISTAR PENDENTES (Ex: GET /api/homologacao/pendentes/63)
    @GetMapping("/pendentes/{idEspec}")
    public ResponseEntity<List<ProntuarioTemporario>> listarPendentes(@PathVariable Integer idEspec) {
        // Atenção: Certifique-se que o método no Repository busca por STATUS_APROVACAO = 'PENDENTE'
        List<ProntuarioTemporario> lista = tempRepo.findPendentesPorEspecialidade(idEspec);
        return ResponseEntity.ok(lista);
    }

    // 2. APROVAR
    @PostMapping("/aprovar/{idTemp}")
    public ResponseEntity<String> aprovar(@PathVariable int idTemp, @RequestParam Integer idSupervisor) {
        Optional<ProntuarioTemporario> opt = tempRepo.findById(idTemp);
        
        if (opt.isPresent()) {
            ProntuarioTemporario temp = opt.get();
            
            // A. Cria o registro na tabela OFICIAL (PRONTUARIO)
            Prontuario oficial = new Prontuario();
            oficial.setIdPaciente(temp.getIdPaciente());
            oficial.setIdProfissio(temp.getAluno().getIdprofissio()); // Aluno como autor
            oficial.setIdEspec(temp.getIdEspec());
            oficial.setIdProced(temp.getIdProced());
            oficial.setDataProced(LocalDate.now()); // Data da homologação
            oficial.setDescrProntu(temp.getTexto());
            oficial.setAutoPacVisu(temp.getAutoPacVisu());
            // Link proced se existir...
            
            oficialRepo.save(oficial); 

            // B. Atualiza o Temporário (Histórico de Decisão)
            temp.setStatus("APROVADO");
            temp.setIdSupervisor(idSupervisor); // Grava quem aprovou
            temp.setDataDecisao(LocalDateTime.now()); // Grava quando
            temp.setObservacaoSupervisor("Aprovado e integrado ao prontuário.");
            
            tempRepo.save(temp);

            return ResponseEntity.ok("Prontuário homologado com sucesso!");
        }
        return ResponseEntity.badRequest().body("Prontuário não encontrado.");
    }

    // 3. REPROVAR / CORRIGIR
    @PostMapping("/corrigir/{idTemp}")
    public ResponseEntity<String> corrigir(@PathVariable int idTemp, 
                                           @RequestParam Integer idSupervisor,
                                           @RequestBody String motivo) {
        Optional<ProntuarioTemporario> opt = tempRepo.findById(idTemp);
        
        if (opt.isPresent()) {
            ProntuarioTemporario temp = opt.get();
            
            // Muda status para REPROVADO (para o aluno corrigir)
            temp.setStatus("REPROVADO"); 
            temp.setIdSupervisor(idSupervisor);
            temp.setDataDecisao(LocalDateTime.now());
            temp.setObservacaoSupervisor(motivo); // Motivo da reprovação
            
            tempRepo.save(temp);
            return ResponseEntity.ok("Devolvido para correção.");
        }
        return ResponseEntity.badRequest().body("Erro ao processar.");
    }
}