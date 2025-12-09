package com.br.fasipe.menubase.biomedicina.repository;

import java.util.List;
import com.br.fasipe.menubase.biomedicina.dto.UsuarioResumoDTO;
import com.br.fasipe.menubase.biomedicina.models.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Integer> {

    @Query(value = "SELECT COUNT(*) FROM PROFISSIONAL p " +
                   "JOIN PROFI_ESPEC pe ON p.IDPROFISSIO = pe.ID_PROFISSIO " +
                   "WHERE p.TIPOPROFI = '4' AND pe.ID_ESPEC = :idEspec", nativeQuery = true)
    int contarMastersPorEspecialidade(@Param("idEspec") Integer idEspec);

    @Query("SELECT new com.br.fasipe.menubase.biomedicina.dto.UsuarioResumoDTO(" +
           "u.id, p.id, pe.nome, u.login, p.tipoProfi, CAST(pe.idDocumento AS string)) " +
           "FROM Profissional p " +
           "JOIN p.especialidades e " +
           "LEFT JOIN p.usuario u " +
           "LEFT JOIN u.pessoaFis pf " +
           "LEFT JOIN Pessoa pe ON pf.idPessoa = pe.idPessoa " +
           "WHERE e.id = :idEspec")
    List<UsuarioResumoDTO> listarPorEspecialidade(@Param("idEspec") Integer idEspec);

    // Remove vínculo da tabela de ligação
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM PROFI_ESPEC WHERE ID_PROFISSIO = :idProfi", nativeQuery = true)
    void desvincularEspecialidades(@Param("idProfi") Integer idProfi);

    // --- NOVO: DELETE NATIVO ---
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM PROFISSIONAL WHERE IDPROFISSIO = :id", nativeQuery = true)
    void deletarPorIdNativo(@Param("id") Integer id);
}