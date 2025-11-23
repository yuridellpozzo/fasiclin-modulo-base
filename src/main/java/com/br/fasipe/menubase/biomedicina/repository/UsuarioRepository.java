package com.br.fasipe.menubase.biomedicina.repository;

import com.br.fasipe.menubase.biomedicina.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByLoginAndSenha(String login, String senha);

    // --- NOVA QUERY SOLICITADA ---
    // Busca o NOMEPESSOA na tabela PESSOA usando o ID_PESSOA
    @Query(value = "SELECT NOMEPESSOA FROM PESSOA WHERE IDPESSOA = :id LIMIT 1", nativeQuery = true)
    String findNomeByIdPessoa(@Param("id") Integer idPessoa);

}