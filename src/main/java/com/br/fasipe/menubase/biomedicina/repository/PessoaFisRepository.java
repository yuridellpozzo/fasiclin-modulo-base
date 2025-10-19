package com.br.fasipe.menubase.biomedicina.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.br.fasipe.menubase.biomedicina.models.PessoaFis;

@Repository
public interface PessoaFisRepository extends JpaRepository<PessoaFis, Integer> {
}
