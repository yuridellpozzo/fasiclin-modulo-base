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

    // --- NOVO: ENDPOINT PARA O ESTAGIÁRIO SALVAR (CRIA O PENDENTE) ---
    @PostMapping("/salvar")
    public ResponseEntity<String> salvarTemporario(@RequestBody ProntuarioTemporario novoProntuario) {
        try {
            // Define status inicial obrigatório conforme seu banco
            novoProntuario.setStatus("PENDENTE"); 
            
            // Define data do procedimento se não vier preenchida
            if (novoProntuario.getDataProced() == null) {
                novoProntuario.setDataProced(LocalDate.now());
            }

            tempRepo.save(novoProntuario);
            return ResponseEntity.ok("Prontuário salvo e enviado para homologação!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar: " + e.getMessage());
        }
    }

    // 1. LISTAR PENDENTES
    @GetMapping("/pendentes/{idEspec}")
    public ResponseEntity<List<ProntuarioTemporario>> listarPendentes(@PathVariable Integer idEspec) {
        // CORREÇÃO: No seu banco o status é 'PENDENTE', não 'AGUARDANDO'
        // Certifique-se que no Repository a query também esteja buscando por 'PENDENTE'
        List<ProntuarioTemporario> lista = tempRepo.findPendentesPorEspecialidade(idEspec);
        return ResponseEntity.ok(lista);
    }

    // 2. APROVAR
    @PostMapping("/aprovar/{idTemp}")
    public ResponseEntity<String> aprovar(@PathVariable int idTemp, @RequestParam Integer idSupervisor) {
        
        Optional<ProntuarioTemporario> opt = tempRepo.findById(idTemp);
        
        if (opt.isPresent()) {
            ProntuarioTemporario temp = opt.get();
            
            // Cria registro Oficial
            Prontuario oficial = new Prontuario();
            oficial.setIdPaciente(temp.getIdPaciente());
            oficial.setIdProfissio(temp.getAluno().getId()); 
            oficial.setIdEspec(temp.getIdEspec());
            oficial.setIdProced(temp.getIdProced());
            oficial.setDataProced(temp.getDataProced()); // Usa a data original do procedimento
            oficial.setDescrProntu(temp.getTexto());
            oficial.setAutoPacVisu(temp.getAutoPacVisu());
            
            oficialRepo.save(oficial); 

            // Atualiza Temporário
            temp.setStatus("APROVADO");
            temp.setIdSupervisor(idSupervisor); 
            temp.setDataDecisao(LocalDateTime.now()); 
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
            
            temp.setStatus("REPROVADO"); 
            temp.setIdSupervisor(idSupervisor);
            temp.setDataDecisao(LocalDateTime.now());
            temp.setObservacaoSupervisor(motivo); 
            
            tempRepo.save(temp);
            return ResponseEntity.ok("Devolvido para correção.");
        }
        return ResponseEntity.badRequest().body("Erro ao processar.");
    }

    // 4. LISTAR CORREÇÕES PENDENTES (Para o Estagiário ver o que voltou)
    @GetMapping("/correcoes/{idAluno}")
    public ResponseEntity<List<ProntuarioTemporario>> listarCorrecoes(@PathVariable Integer idAluno) {
        List<ProntuarioTemporario> lista = tempRepo.findReprovadosPorAluno(idAluno);
        return ResponseEntity.ok(lista);
    }
}