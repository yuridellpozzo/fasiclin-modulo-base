package com.br.fasipe.menubase.biomedicina.repository;

import com.br.fasipe.menubase.biomedicina.models.ProntuarioTemporario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProntuarioTemporarioRepository extends JpaRepository<ProntuarioTemporario, Integer> {
    
    // --- CORREÇÃO: USANDO 'JOIN FETCH' ---
    // O comando 'JOIN FETCH p.aluno' é vital. Ele diz ao banco: 
    // "Traga o Prontuário E TAMBÉM os dados do Aluno dono dele agora mesmo".
    
    // 1. Lista para o Supervisor (PENDENTE)
    @Query("SELECT p FROM ProntuarioTemporario p JOIN FETCH p.aluno WHERE p.idEspec = :idEspec AND p.status = 'PENDENTE'")
    List<ProntuarioTemporario> findPendentesPorEspecialidade(@Param("idEspec") Integer idEspec);

    // 2. Lista para o Estagiário (REPROVADO)
    @Query("SELECT p FROM ProntuarioTemporario p JOIN FETCH p.aluno WHERE p.aluno.id = :idAluno AND p.status = 'REPROVADO'")
    List<ProntuarioTemporario> findReprovadosPorAluno(@Param("idAluno") Integer idAluno);
}