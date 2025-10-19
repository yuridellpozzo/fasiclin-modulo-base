package com.br.fasipe.menubase.biomedicina.repository;

import com.br.fasipe.menubase.biomedicina.models.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Integer> {
    Optional<PessoaJuridica> findByIdPessoa(Integer idPessoa);
    Optional<PessoaJuridica> findByCnpj(String cnpj);
    Optional<PessoaJuridica> findByRazaoSocial(String razaoSocial);
    Optional<PessoaJuridica> findByNomeFantasia(String nomeFantasia);
}
