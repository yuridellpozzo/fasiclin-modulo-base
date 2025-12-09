package com.br.fasipe.menubase.biomedicina.repository;

import com.br.fasipe.menubase.biomedicina.models.ProntuarioTemporario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProntuarioTemporarioRepository extends JpaRepository<ProntuarioTemporario, Integer> {
    
    // Lista para o Supervisor (Status PENDENTE)
    @Query(value = "SELECT * FROM PRONTUARIO_TEMPORARIO WHERE ID_ESPEC = :idEspec AND STATUS_APROVACAO = 'PENDENTE'", nativeQuery = true)
    List<ProntuarioTemporario> findPendentesPorEspecialidade(@Param("idEspec") Integer idEspec);

    // --- NOVO: Lista para o Estagiário (Status REPROVADO e ID dele) ---
    @Query(value = "SELECT * FROM PRONTUARIO_TEMPORARIO WHERE ID_PROFISSIO = :idAluno AND STATUS_APROVACAO = 'REPROVADO'", nativeQuery = true)
    List<ProntuarioTemporario> findReprovadosPorAluno(@Param("idAluno") Integer idAluno);
}