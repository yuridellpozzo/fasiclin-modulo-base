package com.br.fasipe.menubase.biomedicina.repository;

import com.br.fasipe.menubase.biomedicina.models.ProntuarioTemporario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProntuarioTemporarioRepository extends JpaRepository<ProntuarioTemporario, Integer> {
    
    // Busca apenas os que estão AGUARDANDO e são da especialidade certa (ex: Fisio=63)
    @Query(value = "SELECT * FROM PRONTUARIO_TEMPORARIO WHERE ID_ESPEC = :idEspec AND STATUS_HOMOLOGACAO = 'AGUARDANDO'", nativeQuery = true)
    List<ProntuarioTemporario> findPendentesPorEspecialidade(@Param("idEspec") Integer idEspec);
}