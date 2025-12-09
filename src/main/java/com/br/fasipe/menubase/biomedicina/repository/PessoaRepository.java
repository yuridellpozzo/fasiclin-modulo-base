package com.br.fasipe.menubase.biomedicina.repository;

import com.br.fasipe.menubase.biomedicina.models.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

    // --- NOVO: DELETE NATIVO ---
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM PESSOA WHERE IDPESSOA = :id", nativeQuery = true)
    void deletarPorIdNativo(@Param("id") Integer id);
}