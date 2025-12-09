package com.br.fasipe.menubase.biomedicina.repository;

import com.br.fasipe.menubase.biomedicina.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    @Modifying
    @Transactional // AQUI SIM, PRECISA!
    @Query(value = "INSERT INTO DOCUMENTO (DOCUMENTO) VALUES (:id)", nativeQuery = true)
    void salvarDocumentoNaMarra(@Param("id") Long id);
}