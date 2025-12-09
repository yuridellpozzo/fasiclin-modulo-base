package com.br.fasipe.menubase.biomedicina.repository;

import com.br.fasipe.menubase.biomedicina.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByLogin(String login);
    Optional<Usuario> findByLoginAndSenha(String login, String senha);

    @Query(value = "SELECT NOMEPESSOA FROM PESSOA WHERE IDPESSOA = :idPessoa", nativeQuery = true)
    String findNomeByIdPessoa(@Param("idPessoa") Integer idPessoa);

    @Query(value = "SELECT u.* FROM USUARIO u " +
                   "JOIN PESSOAFIS pf ON u.ID_PESSOAFIS = pf.IDPESSOAFIS " +
                   "JOIN PESSOA p ON pf.ID_PESSOA = p.IDPESSOA " +
                   "WHERE p.EMAIL = :email LIMIT 1", nativeQuery = true)
    Optional<Usuario> findByEmailPessoa(@Param("email") String email);

    // --- NOVO: DELETE NATIVO (Ignora erros de memória do Hibernate) ---
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM USUARIO WHERE IDUSUARIO = :id", nativeQuery = true)
    void deletarPorIdNativo(@Param("id") Integer id);
}